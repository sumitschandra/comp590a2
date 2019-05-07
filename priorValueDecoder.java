package src.app;

package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import ac.ArithmeticDecoder;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class priorValueDecoder {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		String input_file_name = "data/context-adaptive-compressed.dat";
		String output_file_name = "data/reuncompressed.txt";

		FileInputStream fis = new FileInputStream(input_file_name);

		InputStreamBitSource bit_source = new InputStreamBitSource(fis);

		Integer[] symbols = new Integer[256];

		for (int i = 0; i < 256; i++) {
			symbols[i] = i;
		}

		// Create 256 models. Model chosen depends on value of symbol prior to
		// symbol being encoded.

		FreqCountIntegerSymbolModel[] countsModels = new FreqCountIntegerSymbolModel[256];

		for (int i = 0; i < 256; i++) {
			// Create new model with default count of 1 for all symbols
			countsModels[i] = new FreqCountIntegerSymbolModel(symbols);
		}

		// Read in number of symbols encoded

		int num_symbols = bit_source.next(32);

		// Read in range bit width and setup the decoder

		int range_bit_width = bit_source.next(8);
		ArithmeticDecoder<Integer> arithmeticDecoder = new ArithmeticDecoder<Integer>(range_bit_width);

		// Decode and produce output.

		System.out.println("Uncompressing file: " + input_file_name);
		System.out.println("Output file: " + output_file_name);
		System.out.println("Range Register Bit Width: " + range_bit_width);
		System.out.println("Number of encoded symbols: " + num_symbols);

		FileOutputStream fos = new FileOutputStream(output_file_name);

		// Use model 0 as initial model.
		FreqCountIntegerSymbolModel freqCountsModel = countsModel[0];
		

		for (int i = 0; i < num_symbols; i++) {
			
			int numberOfFrames = 0;
			int[][] countOfFrames = new int[250][4096];

			if (numberOfFrames != 0) {
				freqCountsModel = countsModel[[i - (4096 * numberOfFrames)]countOfFrames[numberOfFrames - 1]];
			}
			
			if (i >= 4096) {
				numberOfFrames = Div(i, 4096);
				Math.floor(numberOfFrames);
			}
			
			int intSymbol = arithmeticDecoder.decode(freqCountsModel, bit_source);
			
			fos.write(countOfFrames[numberOfFrames][i - (4096 * numberOfFrames)]);
			
			// Update model used
			freqCountsModel.addToCount(intSymbol);
			
			// Set up next model to use.
			freqCountsModel = countsModel[intSymbol];
		}

		System.out.println("Done.");
		
		fos.flush();
		fos.close();
		fis.close();
	}
}
