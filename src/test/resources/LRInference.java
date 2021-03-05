class LRInference {

	public static void main(String[] arg){
		int row_size = 2;
    int num_rows = 2;
    int row_index = 0;
    int coeff_size = 2;
    int i,j = 0;
		EncInt temp = 0;
		EncInt temp2 = 0;
		EncInt temp3 = 0;
    EncInt prod = 0;
    int index = 0;
    int index2 = 0;
    EncInt[] rows = new EncInt[row_size * num_rows];
    EncInt[] coeffs = new EncInt[coeff_size];
    EncInt[] predict = new EncInt[num_rows];
    EncInt[] x_3 = new EncInt[num_rows];
    EncInt[] x_5 = new EncInt[num_rows];

		while (i < num_rows) {
			predict[i] = 0;
			i++;
		}

		i = 0;
    // Read in encrypted test dataset
    while (i < num_rows) {
      j = 0;
      while (j < row_size) {
        index = i * row_size;
        index += j;
        rows[index] = PrivateTape.read();
        j++;
      }
      i++;
    }

    i = 0;

    // Read in encrypted coefficients
    while (i < coeff_size) {
      coeffs[i] = PrivateTape.read();
      i++;
    }

    i = 0;
    while (i < num_rows) {
			prod = 1;
			j = 0;
      while (j < row_size) {
        index = i * row_size;
        index += j;
        prod = prod * (coeffs[j]);
        prod = prod * (rows[index]);
				temp = (predict[i]);
        temp += prod;
				predict[i] = temp;
				j++;
      }
			i++;
    }

    // // Approximate sigmoid
    // // 1/2 + 1/4x - 1/48x^3 + 1/480x^5
    // // 240 + 120x - 10x^3 + x^5 (mult by 48)

    i = 0;
    while (i < num_rows) {
      x_3[i] = predict[i];
			temp = x_3[i];
			temp = temp * (predict[i]);
      temp = temp * (predict[i]);
			x_3[i] = temp;
			temp = temp * (predict[i]);
      temp = temp * (predict[i]);
			x_5[i] = temp;
			temp = 48 + 240;
			temp2 = predict[i];
			temp2 = temp2 * 120;
			temp3 = x_3[i];
			temp3 = temp3 * 10;
			temp += temp2;
			temp -= temp3;
			temp = temp + (x_5[i]);
			predict[i] = temp;
			i++;
    }

    // // Client must divide by 48 and compute 1/prediction for actual result
		i = 0;
		while (i < num_rows) {
			System.out.println(predict[i]);
			i++;
		}
    Processor.answer(predict[0]);
	}
}
