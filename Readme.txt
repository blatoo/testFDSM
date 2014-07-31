Part 1. Generate input Data

Important: Original Netflix Data Set is "data3user.txt"

	Step 1: Set the output root
		info -> Setting.java -> change the "outputRoot" to you wish order
		
	Step 2: Generate the input database
		info -> GenerateInputData.java -> change the "dataSet_Original" to the right path of your original Netflix Data Set(data3user.txt)
		run this Class
		
	Step 3: Choose the users from the input database and create the inputFile for bipartite graph
		info -> DataExtract.java -> choose selectMode1(...) or selectMode2(...)
	
If you have Input Data for VertexVortex, you can convert it directly:
	
	info -> dataConvert.java -> give the inputFile to a String -> run "convert(inputFile);"
	
Part 2. Test the Speed of the part of co-occurrence and swaps

	1. test the co-occurrence
		parts -> ReadCooccTime.java -> run(inputFile)
	2. test the swap speed
		parts -> SwapTime.java -> run(numberOfSwaps, inputFile)


 
