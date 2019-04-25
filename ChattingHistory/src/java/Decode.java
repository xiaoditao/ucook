/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




public class Decode {

	public String[] decrypt(String input) {
		String[] reString=new String[2];
		String[] info = input.split("a");
		String originId = info[0];
		String originUsername = info[1];
		try {
		int three1 = originId.length()/3;
		int three2 = originUsername.length()/3;
		String resId = "";
		String resUsername = "";
		for(int i = 0;i<three1;i++) {
			String slide = originId.substring(i*3, i*3+3);
			int trans = Integer.valueOf(slide) ;
			char tempt = (char)trans;
			String temptString = String.valueOf(tempt);
			resId+=temptString;
		}
		for(int i = 0;i<three2;i++) {
			String slide = originUsername.substring(i*3, i*3+3);
			int trans = Integer.valueOf(slide);
			
			char tempt = (char)trans;
			String temptString = String.valueOf(tempt);
			resUsername+=temptString;
		}
		reString[0] = resId;
		reString[1] = resUsername;
		}catch(Exception e){
			System.out.println("error");
		}
		return reString;
	}


 
}
