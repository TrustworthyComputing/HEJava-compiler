package org.twc.hejavacompiler.kanga2asm;

public class AsmPrinter {

    private int indent_;
    private final StringBuilder asm_;
    private boolean newline_;

    public AsmPrinter() {
        indent_ = 0;
        asm_ = new StringBuilder();
        newline_ = true;
    }

    public void print(String s) {
        if (newline_) {
            for (int i = 0; i < indent_; i++) {
                asm_.append("\t");
            }
            newline_ = false;
        }
        asm_.append(s).append(" ");
    }

    public void printLabel(String s) {
        if ("__Runtime_Error__".equals(s)) return;
        asm_.append(s).append(":\n");
    }

    public void println(String s) {
        if (newline_) {
            for (int i = 0; i < indent_; i++) {
                asm_.append("\t");
            }
        }
        asm_.append(s).append("\n");
        newline_ = true;
    }

    public void println() {
        asm_.append("\n");
        newline_ = true;
    }

    public void begin(String method) {
        indent_ = 0;
        println("__" + method + "__:");
        indent_ = 2;
    }

    public void end() {
        indent_ = 0;
        println();
    }

    public String toString() {
        return asm_.toString();
    }

}