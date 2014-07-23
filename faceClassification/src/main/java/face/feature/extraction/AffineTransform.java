package face.feature.extraction;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWStructArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import affineTransform.Absor;

public class AffineTransform {
	public static final double[][] keyCoordinate={{60,92},{190,242},
												  {68,128},{182,153},
												  {100,200},{150,225},
												  {60,40},{190,75},
												  {108,127},{142,187},
												  {155,160},{180,220},
												  {98,230},{152,250},
												  {73,90},{175,106},
												  {60,110},{190,125},
												  {100,188},{150,200},
												  {10,10},{260,225}};
	
	public static Double[][] getKeyPixels(double[][] arrayTrg) {
		Absor test = null;
		Object[] computResult = null;
		MWStructArray resultArray = null; 
		MWNumericArray source = null;
		MWNumericArray target = null;
		double[][] arraySrc = {{73,106,144,177,100,152},{142,143,143,142,208,208}};
		String[] parameter1 = {"doScale"};
		String[] parameter2 = {"TRUE"};
		
		MWArray resultR = null;
		MWArray resultT = null;
		MWArray resultS = null;
		double[][] R= null;
		double[][] t= null;
		double s = 1;
		Double[][] result = new Double[keyCoordinate.length][keyCoordinate[0].length];
		try {
			test = new Absor();
			source = new MWNumericArray(arraySrc);
			target = new MWNumericArray(arrayTrg);
			computResult = test.absor(1, arraySrc, arrayTrg, parameter1, parameter2);
			
			resultArray = (MWStructArray)computResult[0];
			
			resultR = resultArray.getField("R", 1);
			R = (double[][])resultR.toArray();
			
			resultT = resultArray.getField("t", 1);
			t = (double[][])resultT.toArray();
			
			resultS = resultArray.getField("s", 1);
			s = Double.valueOf(resultS.toString());
			for (int i = 0; i < keyCoordinate.length; i++) {
				result[i][0] = s*(R[0][0]*keyCoordinate[i][0]+R[0][1]*keyCoordinate[i][1])+t[0][0];
				result[i][1] = s*(R[1][0]*keyCoordinate[i][0]+R[1][1]*keyCoordinate[i][1])+t[1][0];
			}
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			test.dispose();
			MWArray.disposeArray(source);
			MWArray.disposeArray(target);
			MWArray.disposeArray(resultR);
			MWArray.disposeArray(resultT);
			MWArray.disposeArray(resultS);
			MWStructArray.disposeArray(resultArray);
		}
		return result;
	}
	
	public static void main(String[] args) {
		double[][] arrayTrg = {{35,50,71,87,41,79},{69,71,73,72,104,106}};
		Double[][] result = getKeyPixels(arrayTrg);
		for (Double [] res: result){
			System.out.print(res[0]);
			System.out.println("abc");
		}
	}
	
	
}
