package util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Functions {

	public static void arrayPrintTwoDimensions(int[][] arr) {

		for (int i = 0; i < arr.length; i++) {

			System.out.print("line " + i + " : ");

			for (int j = 0; j < arr[i].length; j++) {

				System.out.print(arr[i][j] + ",");
			}

			System.out.print(System.lineSeparator());

		}

	}
	
	public static void arrayPrintTwoDimensions(short[][] arr) {

		for (int i = 0; i < arr.length; i++) {

			System.out.print("line " + i + " : ");

			for (int j = 0; j < arr[i].length; j++) {

				System.out.print(arr[i][j] + ",");
			}

			System.out.print(System.lineSeparator());

		}

	}
	
	public static void arrayPrintTwoDimensions(double[][] arr) {

		for (int i = 0; i < arr.length; i++) {

			System.out.print("line " + i + " : ");

			for (int j = 0; j < arr[i].length; j++) {

				System.out.print(arr[i][j] + ",");
			}

			System.out.print(System.lineSeparator());

		}

	}
	
	
	public static <T extends Number> void arrayPrintTwoDimensions(T[][] arr) {

		for (int i = 0; i < arr.length; i++) {

			System.out.print("line " + i + " : ");

			for (int j = 0; j < arr[i].length; j++) {

				System.out.print(arr[i][j] + ",");
			}

			System.out.print(System.lineSeparator());

		}

	}

	public static void arrayPrintTwoDimensions(Integer[][] arr) {

		for (int i = 0; i < arr.length; i++) {

			System.out.print("line " + i + " : ");

			for (int j = 0; j < arr[i].length; j++) {

				System.out.print(arr[i][j] + ",");
			}

			System.out.print(System.lineSeparator());

		}

	}

	public static void main(String[] args) {

	}

}
