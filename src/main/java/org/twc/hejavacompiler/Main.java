package org.twc.hejavacompiler;

import org.twc.hejavacompiler.basetype.Class_t;
import org.twc.hejavacompiler.basetype.Method_t;
import org.twc.hejavacompiler.spiglet2kanga.spigletsyntaxtree.Node;
import org.twc.hejavacompiler.spigletoptimizer.SpigletOptimizer;
import org.twc.hejavacompiler.hejava2spiglet.SymbolTableVisitor;
import org.twc.hejavacompiler.hejava2spiglet.TypeCheckVisitor;
import org.twc.hejavacompiler.hejava2spiglet.VisitClasses;
import org.twc.hejavacompiler.hejava2spiglet.HEJava2Spiglet;
import org.twc.hejavacompiler.hejava2spiglet.hejavaparser.ParseException;
import org.twc.hejavacompiler.hejava2spiglet.hejavaparser.HEJavaParser;
import org.twc.hejavacompiler.spiglet2kanga.GetFlowGraph;
import org.twc.hejavacompiler.spiglet2kanga.GetFlowGraphVertex;
import org.twc.hejavacompiler.spiglet2kanga.Spiglet2Kanga;
import org.twc.hejavacompiler.spiglet2kanga.Temp2Reg;
import org.twc.hejavacompiler.spiglet2kanga.spigletparser.SpigletParser;
import org.twc.hejavacompiler.kanga2asm.Kanga2asm;
import org.twc.hejavacompiler.kanga2asm.kangaparser.KangaParser;
import org.twc.hejavacompiler.hejava2spiglet.hejavasyntaxtree.Goal;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    private static boolean debug_ = false;
    private static boolean enable_opts_ = false;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("fatal error: no input files.");
            System.exit(-1);
        }
        ArrayList<String> input_files = new ArrayList<>();
        for (String arg : args) {
            if (arg.toUpperCase().equals("-DEBUG") || arg.toUpperCase().equals("--DEBUG")) {
                debug_ = true;
            } else if (arg.toUpperCase().equals("-OPTS") || arg.toUpperCase().equals("--OPTS")) {
                enable_opts_ = true;
            } else {
                input_files.add(arg);
            }
        }

        Properties memory_properties = new Properties();
        try {
            memory_properties.load(new FileInputStream("src/main/java/org/twc/hejavacompiler/zilch-memory.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (debug_) {
            memory_properties.list(System.out);
        }
        final int init_heap_offset = Integer.parseInt(memory_properties.getProperty("Initial_Heap_Offset"));
        final int init_stack_offset = Integer.parseInt(memory_properties.getProperty("Initial_Stack_Offset"));

        for (String arg : input_files) {
            InputStream input_stream = null;
            PrintWriter writer = null;
            File fp = new File(arg);
            String path = fp.getPath();
            path = path.substring(0, path.lastIndexOf('.'));
            try {
                System.out.println("===================================================================================");
                System.out.println("Compiling file \"" + arg + "\"");
                input_stream = new FileInputStream(arg);
                // hejava typechecking
                HEJavaParser hejava_parser = new HEJavaParser(input_stream);
                Goal hejava_root = hejava_parser.Goal();
                VisitClasses firstvisit = new VisitClasses();
                hejava_root.accept(firstvisit);
                System.out.println("[ 1/3 ] Class name collection phase completed");
                SymbolTableVisitor symtable_visit = new SymbolTableVisitor(firstvisit.getClassList());
                hejava_root.accept(symtable_visit);
                Map<String, Class_t> symbol_table = symtable_visit.getSymbolTable();
                if (debug_) {
                    System.out.println();
                    symtable_visit.printSymbolTable();
                }
                System.out.println("[ 2/3 ] Class members and methods info collection phase completed");
                TypeCheckVisitor type_checker = new TypeCheckVisitor(symbol_table);
                hejava_root.accept(type_checker, null);
                System.out.println("[ 3/3 ] Type checking phase completed");
                System.out.println("[ \033[0;32m \u2713 \033[0m ] All checks passed");

                // generate Spiglet code
                HEJava2Spiglet hejava2spiglet = new HEJava2Spiglet(symbol_table, symtable_visit.getGlobalsNumber(), init_heap_offset);
                hejava_root.accept(hejava2spiglet, null);
                int hp_ = hejava2spiglet.getHP();
                String code = hejava2spiglet.getASM();
                if (debug_) {
                    String spiglet_output_path = path + ".spg";
                    writer = new PrintWriter(spiglet_output_path);
                    writer.print(code);
                    writer.close();
                    System.out.println(code);
                    System.out.println("[ \033[0;32m \u2713 \033[0m ] Spiglet code generated to \"" + spiglet_output_path + "\"");
                    input_stream = new FileInputStream(spiglet_output_path);
                } else {
                    System.out.println("[ \033[0;32m \u2713 \033[0m ] Generated Spiglet code");
                    input_stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
                }

                // optimize code
                if (enable_opts_) {
                    SpigletOptimizer spq_optimizer = new SpigletOptimizer(debug_);
                    input_stream = spq_optimizer.performOptimizations(input_stream, fp.getPath());
                }

                // generate Kanga code
                SpigletParser spiglet_parser = new SpigletParser(input_stream);
                Node spiglet_ast = spiglet_parser.Goal();
                HashMap<String, Method_t> method_map_ = new HashMap<>();
                HashMap<String, Integer> mLabel = new HashMap<>();
                spiglet_ast.accept(new GetFlowGraphVertex(method_map_, mLabel));
                spiglet_ast.accept(new GetFlowGraph(method_map_, mLabel));
                System.out.println("[ 1/3 ] Flow graph creation phase completed");
                new Temp2Reg(method_map_).LinearScan();
                System.out.println("[ 2/3 ] Linear scan on flow graph phase completed");
                Spiglet2Kanga spiglet2kanga = new Spiglet2Kanga(method_map_);
                spiglet_ast.accept(spiglet2kanga);
                System.out.println("[ 3/3 ] Register allocation phase completed");
                code = spiglet2kanga.getASM();
                if (debug_) {
                    System.out.println(code);
                    String kanga_output_path = path + ".kg";
                    writer = new PrintWriter(kanga_output_path);
                    writer.print(code);
                    writer.close();
                    System.out.println("[ \033[0;32m \u2713 \033[0m ] Kanga code generated to \"" + kanga_output_path + "\"");
                    input_stream = new FileInputStream(kanga_output_path);
                } else {
                    System.out.println("[ \033[0;32m \u2713 \033[0m ] Generated Kanga code");
                    input_stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
                }

                // generate assembly code from Kanga
                KangaParser kanga_parser = new KangaParser(input_stream);
                org.twc.hejavacompiler.kanga2asm.kangasyntaxtree.Node kanga_ast = kanga_parser.Goal();
                Kanga2asm kanga2asm = new Kanga2asm(init_heap_offset, init_stack_offset, hp_, spiglet2kanga.hasProcedures(), hejava2spiglet.mayHasError());
                kanga_ast.accept(kanga2asm);
                String asm_output_path = path + ".asm";
                writer = new PrintWriter(asm_output_path);
                writer.print(kanga2asm.getASM());
                writer.close();
                if (debug_) {
                    System.out.println(kanga2asm.getASM());
                }
                System.out.println("[ \033[0;32m \u2713 \033[0m ] Assembly code generated to \"" + asm_output_path + "\"");
                System.out.println("===================================================================================");
            } catch (ParseException
                    | org.twc.hejavacompiler.spiglet2kanga.spigletparser.ParseException
                    | org.twc.hejavacompiler.kanga2asm.kangaparser.ParseException
                    | FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            } finally {
                try {
                    if (input_stream != null) input_stream.close();
                    if (writer != null) writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

}
