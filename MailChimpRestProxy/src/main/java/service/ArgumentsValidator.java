package service;

import java.util.ArrayList;

public class ArgumentsValidator {
	
	public static Result checkArgs(String[] args) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if(args.length == 3 || args.length == 4) {
			if(args[0] == null ^ args[0].length() == 0) {
				errors.add("Argument error! First argument must be a valid string.");
			}
			if(args[1] == null ^ args[1].length() == 0) {
				errors.add("Argument error! Second argument must be a valid string.");
			}
			if(args[2] == null ^ args[2].length() == 0) {
				errors.add("Argument error! Third argument must be a valid string.");
			}
			if(args.length == 4) {
				if(args[3] == null ^ !isPositiveInteger(args[3])) {
					errors.add("Argument error! Fourth argument must be a positive integer.");
				}
			}
		}else {
			errors.add("Argument structure error! Please use the following input arguments: \n <operation type> <config file path> <input file path> [<maximal operation duration in ms>]");
		}
		
		return new Result(errors);
	}
	
	private static boolean isPositiveInteger(String s) {
	    int i;
		try { 
	        i = Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    if(i>0) {
	    	return true;
	    }else {
	    	return false;
	    }
	}
	
	public static class Result{
		private final boolean isCorrect;
		private final ArrayList<String> errors;
		
		public Result(ArrayList<String> errors) {
			this.errors=errors;
			if(this.errors.size() == 0) {
				this.isCorrect = true;
			}else{
				this.isCorrect = false;
			}
		}
		
		public boolean isCorrect() {
			return this.isCorrect;
		}
		
		public ArrayList<String> getErrors(){
			return this.errors;
		}
	}
}
