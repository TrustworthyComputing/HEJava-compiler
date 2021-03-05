class MatrixMultiplication {

	public static void main(String[] arg){
		int i,j,k;
		EncInt x,y,z;
		int rows_1, cols_1_rows_2, cols_2;
		EncInt[] a;
		EncInt[] b;
		EncInt[] res;
		rows_1 = PublicTape.read();
		cols_1_rows_2 = PublicTape.read();
		cols_2 = PublicTape.read();
		a = new EncInt[rows_1 * cols_1_rows_2];
		b = new EncInt[cols_1_rows_2 * cols_2];
		res = new EncInt[rows_1 * cols_2];

		i = 0;
		while (i < (rows_1 * cols_1_rows_2)) {
			a[i] = PrivateTape.read();
			i++;
		}

		i = 0;
		while (i < (cols_1_rows_2 * cols_2)) {
			b[i] = PrivateTape.read();
			i++;
		}

		i = 0;
		while (i < (rows_1 * cols_2)) {
			res[i] = 0;
			i++;
		}

		i = 0;
		while (i < rows_1) {
			j = 0;
			while (j < cols_2) {
				k = 0;
				while (k < cols_1_rows_2) {
					x = res[((i*cols_2) + j)];
					y = a[((i * cols_1_rows_2) + k)];
					z = b[((k * cols_2) + j)];
					res[((i*cols_2) + j)] = x + (y * z);
					k = k + 1;
				}
				j = j + 1;
			}
			i = i + 1;
		}

		i = 0;
		while (i < (rows_1 * cols_2)) {
			System.out.println(res[i]);
			i = i + 1;
		}

		Processor.answer(a[0]);
	}

}
