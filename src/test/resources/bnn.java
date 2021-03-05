class BNNInference {

	public static void main(String[] arg){
		int input_shape = 28;
		int theta_rows = 10;
		int theta_columns = 800;
		int num_conv1_filters = 8;
		int filter_shape = 5;
		int num_conv2_filters = 8;
		int conv1_image_size = 24;
		int conv2_image_size = 20;
		int filt1_channels = 1;
		int filt2_channels = 8;
		EncInt[] image = new EncInt[784];
		int sum = 0;
		int[] theta3 = new int[8000];
		EncInt[] conv1 = new EncInt[4608];
		EncInt[] conv2 = new EncInt[3200];
		int[] filt1 = new int[200];
		int[] filt2 = new int[1600];
		int temp, temp2, temp3, temp4, temp5, thresh1, thresh2;

		// Load encrypted image
		int i,j,k,l,x,y = 0;
		while (i < input_shape) {
			while (j < input_shape) {
				temp = i * input_shape;
				image[temp + j] = PrivateTape.read();
				j++;
			}
			i++;
		}

		// Load filters for conv1
		i = 0;
		j = 0;
		while (i < num_conv1_filters) {
			while (j < filt1_channels) {
				while (k < filter_shape) {
					while (l < filter_shape) {
						//filt1[i][j][k][l] = PublicTape.read();
						temp = i * num_conv1_filters;
						temp2 = j * filt1_channels;
						temp3 = k * filter_shape;
						temp4 = temp + temp2;
						temp4 += temp3;
						filt1[temp4 + l] = PublicTape.read();
						l++;
					}
					k++;
				}
				j++;
			}
			i++;
		}

		// Load filters for conv2
		i = 0;
		j = 0;
		k = 0;
		l = 0;

		while (i < num_conv2_filters) {
			while (j < filt2_channels) {
				while (k < filter_shape) {
					while (l < filter_shape) {
						temp = i * num_conv2_filters;
						temp2 = j * filt2_channels;
						temp3 = k * filter_shape;
						temp4 = temp + temp2;
						temp4 += temp3;
						filt2[temp4 + l] = PublicTape.read();
						l++;
					}
					k++;
				}
				j++;
			}
			i++;
		}


		conv1_image_size = input_shape - filter_shape;
		conv1_image_size += 1;
		conv2_image_size = conv1_image_size - filter_shape;
		conv2_image_size += 1;

 		// First Convolution
		i = 0;
		j = 0;
		k = 0;
		while (i < num_conv1_filters) {
			while (j < conv1_image_size) {
				while (k < conv1_image_size) {
					x = j;
					sum = 0;
					thresh1 = j + filter_shape;
					while (x < thresh1) {
						y = k;
						thresh2 = k + filter_shape;
						while (y < thresh2) {
							temp = x * input_shape;
							temp2 = i * num_conv1_filters;
							temp3 = x - j;
							temp3 = temp3 * filter_shape;
							temp4 = y - k;
							temp5 = temp2 + temp3;
							temp5 += temp4;
							temp3 = temp + y;
							temp2 = (image[temp3]);
							temp2 = temp2 * (filt1[temp5]);
							sum += temp2;
							y++;
						}
						x++;
					}
					temp = i * num_conv1_filters;
					temp2 = j * conv1_image_size;
					temp2 += temp;
					conv1[temp + k] = sum;
					k++;
				}
				j++;
			}
			i++;
		}
//
// 		// First ReLU
// 		i = 0;
// 		j = 0;
// 		k = 0;
// 		while (i < num_conv1_filters) {
// 			while (j < conv1_image_size) {
// 				while (k < conv1_image_size) {
// 					conv1[i][j][k] = conv1[i][j][k] * conv1[i][j][k]
// 					k++;
// 				}
// 				j++;
// 			}
// 			i++;
// 		}
//
// 		// Second Convolution
// 		i = 0;
// 		j = 0;
// 		k = 0;
// 		x = 0;
// 		y = 0;
// 		int z = 0;
// 		while (i < num_conv2_filters) {
// 			while (j < conv2_image_size) {
// 				while (k < conv2_image_size) {
// 					sum = 0;
// 					while (z < num_conv1_filters) {
// 						x = j;
// 						while (x < x + filter_shape) {
// 							y = k;
// 							while (y < y + filter_shape) {
// 								sum += conv1[z][x][y] * filt2[i][0][x-j][y-k];
// 								y++;
// 							}
// 							x++;
// 						}
// 						z++;
// 					}
// 					conv2[i][j][k] = sum;
// 					k++;
// 				}
// 				j++;
// 			}
// 			i++;
// 		}
//
// 		// Second ReLU
// 		i = 0;
// 		j = 0;
// 		k = 0;
// 		while (i < num_conv2_filters) {
// 			while (j < conv2_image_size) {
// 				while (k < conv2_image_size) {
// 					conv2[i][j][k] = conv2[i][j][k] * conv2[i][j][k]
// 					k++;
// 				}
// 				j++;
// 			}
// 			i++;
// 		}
//
// 		// Sum Pooling
// 		int[][][] pool = new int[conv2_image_size/2][conv2_image_size/2][num_conv2_filters];
// 		i = 0;
// 		j = 0;
// 		k = 0;
// 		while (i < conv2_image_size - 2) {
// 			while (j < conv2_image_size - 2) {
// 				while (k < num_conv2_filters) {
// 					pool[i/2][j/2][k] = conv2[i][j][k] + conv2[i+1][j][k] + conv2[i][j+1][k] + conv2[i+1][j+1][k];
// 					k++;
// 				}
// 				j = j + 2;
// 			}
// 			i = i + 2;
// 		}
	}
}
