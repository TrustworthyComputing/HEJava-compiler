class LRInference {

	public static void main(String[] arg){
		int row_size = 3;
    int num_rows = 10;
    int row_index = 0;
    int coeff_size = 3;
    int i,j = 0;
		int temp = 0;
		int temp2 = 0;
		int temp3 = 0;
    int prod = 0;
    int index = 0;
    int index2 = 0;
    int[] rows = new int[row_size * num_rows];
    int[] coeffs = new int[coeff_size];
    int[] predict = new int[row_size];
    int[] x_3;
    int[] x_5;

    // Read in encrypted test dataset
    while (i < num_rows) {
      j = 0;
      while (j < row_size) {
        index = i * row_size;
        index = index + j;
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
    j = 0;
    yhat = coeffs[0];
    row_index = row_size - 1;
    while (i < num_rows) {
      while (j < row_index) {
        index = i * row_size;
        index = index + j;
        index2 = j + 1;
        prod = coeffs[index2];
        prod *= rows[index];
				temp = predict[i];
        temp += prod;
				predict[i] = temp;
				j++;
      }
			i++;
    }

    // Approximate sigmoid
    // 1/2 + 1/4x - 1/48x^3 + 1/480x^5
    // 240 + 120x - 10x^3 + x^5 (mult by 48)

    i = 0;
    while (i < num_rows) {
      x_3[i] = predict[i];
			temp = x_3[i];
			temp *= predict[i];
      temp *= predict[i];
			x_3[i] = temp;
			temp *= predict[i];
      temp *= predict[i];
			x_5[i] = temp;
			temp = 48 + 240;
			temp2 = predict[i];
			temp2 *= 120;
			temp3 = x_3[i];
			temp3 *= 10;
			temp += temp2;
			temp -= temp3;
			temp += x_5[i];
			predict[i] = temp;
			i++;
    }

    // Client must divide by 48 and compute 1/prediction for actual result
    Processor.answer(predict);
	}
}
