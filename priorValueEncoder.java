package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import ac.ArithmeticEncoder;
import io.OutputStreamBitSink;

public class priorValueEncoder {

	public static void main(String[] args) throws IOException {
		String input_file_name = "data/uncompressed.txt";
		String output_file_name = "data/context-adaptive-compressed.dat";

		int range_bit_width = 40;

		System.out.println("Encoding text file: " + input_file_name);
		System.out.println("Output file: " + output_file_name);
		System.out.println("Range Register Bit Width: " + range_bit_width);

		int num_symbols = (int) new File(input_file_name).length();

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

		ArithmeticEncoder<Integer> arithmeticEncoder = new ArithmeticEncoder<Integer>(range_bit_width);

		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);

		// First 4 bytes are the number of symbols encoded
		bit_sink.write(num_symbols, 32);

		// Next byte is the width of the range registers
		bit_sink.write(range_bit_width, 8);

		// Now encode the input
		FileInputStream fis = new FileInputStream(input_file_name);

		// Use model 0 as initial model.
		FreqCountIntegerSymbolModel freqCountsModel = countsModel[0];


		for (int i = 0; i < num_symbols; i++) {
			int numberOfFrames = 0;
			int[][] countOfFrames = new int[250][4096];

			int symbol = fis.read();
			
			if (numberOfFrames != 0) {
				freqCountsModel = countsModels[frame[numberOfFrames - 1][i - (4096 * numberOfFrames)]];
			}
			
			if (i >= 4096) {
				numberOfFrames = Div(i, 4096);
				Math.floor(numberOfFrames);			
			}
			
			[i - (4096 * numberOfFrames)]frames[numberOfFrames] = symbol;
			
			
			
			arithmeticEncoder.encode(symbol, freqCountsModel, bit_sink);
			
			// Update model used
			freqCountsModel.addToCount(symbol);
			
			// Set up next model based on symbol just encoded
			freqCountsModel = countsModels[symbol];
		}
		
		fis.close();

		// Finish off by emitting the middle pattern
		// and padding to the next word

		arithmeticEncoder.emitMiddle(bit_sink);
		bit_sink.padToWord();
		fos.close();

		System.out.println("Done");

	}
}
