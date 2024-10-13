import Jama.*; 

//necessary changes, void to voidy

public class TriaxialTest {
	
	String name; //Name for samples
	
	//initial Consolidation pressure, Confining pressure, Critical Friction Angle, Lamda, kappa, N, poissons
	double cp, p0, M, l, k, N, nu;
    double cpiu = -1, p0iu, Miu, liu, kiu, Niu, nuiu, strstepsi;
    
    
    //Dunno, Specific volume, Initial Void Ratio, Over Consolidation Ratio
    double pc, V, e0, OCR;
    
    Matrix es;
    int iter;
    double ide, de;

    int analysis = 1; //drained = 1, undrained = 2
    
    Matrix De, dfds, dfdep, u, p, q, dStrain, voidy, epsV, epsD;
    
    
    Matrix p_ini_yield, q_ini_yield, qy;
    
    
    Matrix S, strain;
    
    
    double yield, K, G, depsV, depsD, Gamma;
    Matrix D;
    Matrix dS, pNCL, pCSL, qNCL, eNCL, p_fyield, q_fyield, qyf, eCSL;
	
    //Additional functionality reacquired for matrix manipulation
    Matrix matPow(Matrix mat, double pow) {
        Matrix newMat = mat.copy();
        for (int i = 0; i < mat.getRowDimension(); i++)
            for (int l = 0; l < mat.getColumnDimension(); l++) {
                newMat.set(i, l, Math.pow(mat.get(i, l), pow));
            }
        return newMat;
    }
    Matrix matPlus(Matrix mat, double plus) {
        Matrix newMat = mat.copy();
        for (int i = 0; i < mat.getRowDimension(); i++)
            for (int l = 0; l < mat.getColumnDimension(); l++) {
                newMat.set(i, l, mat.get(i, l) + plus);
            }
        return newMat;
    }
    Matrix matLog(Matrix mat) {
        Matrix newMat = mat.copy();
        for (int i = 0; i < mat.getRowDimension(); i++)
            for (int l = 0; l < mat.getColumnDimension(); l++) {
                newMat.set(i, l, Math.log(mat.get(i, l)));
            }
        return newMat;
    }
    Matrix printMatrix(Matrix mat) {
        Matrix newMat = mat.copy();
        for (int i = 0; i < mat.getRowDimension(); i++) {
            for (int l = 0; l < mat.getColumnDimension(); l++) {
                System.out.print(newMat.getArrayCopy()[i][l]+"");
            }
            System.out.println("");
        }
        return newMat;
    }
    
    double[][] zeros(int x, int y) {
        return new double[x][y];
    }
    
    //Doing sample and name
    TriaxialTest(String s, double Mi, double li, double ki, double Ni, double nui) {
    	name = s;
        Miu = Mi;
        liu = li;
        kiu = ki;
        Niu = Ni;
        nuiu = nui;
        
        M = Mi;
        l = li;
        k = ki;
        N = Ni;
        nu = nui;
    }
    
    //Only doing sample
    TriaxialTest(double Mi, double li, double ki, double Ni, double nui) {
        Miu = Mi;
        liu = li;
        kiu = ki;
        Niu = Ni;
        nuiu = nui;
        
        M = Mi;
        l = li;
        k = ki;
        N = Ni;
        nu = nui; 
    }

    //Redo testing parameters
    public void runTest(double cpi, double p0i, int ana, int iteration, double strsteps) throws Exception {
    	cpiu = cpi;
        p0iu = p0i;
        
        cp = cpi;
        p0 = p0i;
        analysis = ana;
        strstepsi = strsteps;
        
      //Computation of Other Parameters (V,e0 and OCR)
        pc = cp;
        V = N - (l * Math.log(pc)) + (k * Math.log(pc/p0));
        e0 = V - 1;
        OCR = cp / p0;
        
        //Strain Increment and Strain Matrix Definition
        if (iteration < 7500)
            iter = 7500;
        else
            iter=iteration;
        
        if (strsteps > 0.01 || strsteps <= 0.)
            ide=0.01;
        else 
            ide=strsteps;
        
        de = ide/100;
        
        es = new Matrix(1, iter);
        
        double ic = 0;
        for (int i = 0; i < iter; ic = ic + ide, i++)
            es.set(0, i, ic);
        
        
        
        //Block memory
        De = new Matrix(6, 6);
        dfds = new Matrix(6, 1);
        dfdep = new Matrix(6, 1);
        u = new Matrix(iter, 1);
        p = new Matrix(iter, 1);
        q = new Matrix(iter, 1);
        dStrain = new Matrix(6, 1);
        voidy = new Matrix(iter, 1);
        epsV = new Matrix(iter, 1);
        epsD = new Matrix(iter, 1);

        
        
        // Yield Surface and Conditions
        p_ini_yield = new Matrix((int)pc, 1);       
        for (int i = 0; i < (int)pc; i++)
            p_ini_yield.set(i, 0, i);
        q_ini_yield = p_ini_yield.times(M);
        //Plot the initial yield locus
        qy = matPow(((p_ini_yield.times(pc)).minus(matPow(p_ini_yield,2))).times(Math.pow(M, 2)), 0.5);

        
        //initialise
        int a = 0; //iterator
        //Stress
        double[] nin = {p0, p0, p0, 0, 0, 0};
        S = new Matrix(nin, 1);
        //Strain
        double[] bin = {0,0,0,0,0,0};
        strain = new Matrix(bin, 1);
        
        p.set(0, a, (S.get(0,0) + 2*S.get(0,2)) / 3);
        q.set(0, a, S.get(0,0) - S.get(0,2));
        yield = (Math.pow(q.get(0, a), 2) / Math.pow(M, 2) + Math.pow(p.get(0, a),2)) - p.get(0, a)*pc; // Defining the yield surface
        voidy.set(0, a, e0);
        

        //CamClay Iteration Uni-Loop Iteration for OC/NC & Inside/Outside Yield
        while (a < iter - 1) {
        //while (a < 1) {
            K=V*p.get(a, 0)/k;                     // Bulk Modulus
            G=(3*K*(1-2*nu))/(2*(1+nu));      // Shear Modulus
            
            if (yield == 0) {
                pc = (Math.pow(q.get(a, 0), 2.0)/Math.pow(M, 2.0) + Math.pow(p.get(a, 0), 2.0))/p.get(a, 0);
            } else 
                pc = cp;
            //Elastic Stiffness and other Matrices
            for (int m = 0; m < 6; m++) {
                for (int n = 0; n < 6; n++) {
                    if (m <= 2) {
                        if (yield < 0) {
                            dfds.set(m, 0, 0);
                            dfdep.set(m, 0, 0);
                        } else {
                            dfds.set(m, 0, (2.0*p.get(a, 0)-pc)/3.0 + 3.*(S.get(0, m)-p.get(a, 0))/Math.pow(M,2));
                            dfdep.set(m, 0, (-p.get(a, 0))*pc*(1.0 + e0)/(l-k)*1.0);
                        }
                        if (m == n) {
                            De.set(n, m, K+4.0/3.0*G); //Elastic Stiffness
                        } else if (n<=2) {
                            //De.set(m, n, K-2/3*G);
                            De.set(n, m, K-2.0/3.0*G);
                        }
                    }
                    if (m>2) {
                        dfds.set(m, 0, 0);
                        dfdep.set(m, 0, 0); //%df/ds' and %df/dep
                       
                        if (m==n) {
                            De.set(n, m, G);  //Elastic Stiffness
                        } else  
                            De.set(n, m, 0);
                    }
                }
            }

           //Stiffness Matrix 
           if (yield<0) {
               D=De; //Elastic
           } else 
               D = De.minus((De.times(dfds.times((dfds.transpose()).times(De)))).times(1/(((dfds.transpose()).times(De)).times(dfds).minus(dfdep.transpose().times(dfds))).getArrayCopy()[0][0]));

           //Stress and Strain Updates
            if (analysis==1) { //Triaxial Drained
                double[] tempStrain = {de, -1.0*D.get(1, 0)/(D.get(1, 1)+D.get(1, 2))*de, -1.0*D.get(2, 0)/(D.get(2, 1)+D.get(2, 2))*de, 0, 0, 0};
                dStrain = new Matrix(tempStrain, 1);
            } else if (analysis==2) { //Triaxial Undrained
                double[] tempStrain = {de, -de/2.0, -de/2.0, 0, 0, 0};
                dStrain = new Matrix(tempStrain, 1);
            } else { //Oedometer Drained (analysis=3)
                double[] tempStrain = {de, 0, 0, 0, 0, 0};
                dStrain = new Matrix(tempStrain, 1);
            }

            dS = dStrain.times(D);
            S = S.plus(dS);
            strain = strain.plus(dStrain);

            depsV = dStrain.get(0, 0) + dStrain.get(0, 1) + dStrain.get(0, 2); // Incremental Volumetric Strain
            depsD = 2.0/3.0 * (dStrain.get(0, 1) - dStrain.get(0, 3));      // Incremental Deviatoric Strain



            //Update Specific Volume
            V=N-(l*Math.log(pc))+(k*Math.log(pc/p.get(a, 0)));


            //Subsequent cycle update
            a = a+1;

            p.set(a, 0, (S.get(0, 0) + S.get(0, 1) + S.get(0, 2))/3);
            q.set(a, 0, S.get(0, 0) - S.get(0, 2));
            u.set(a, 0, p0 + q.get(a, 0)/3.0 - p.get(a, 0));

            voidy.set(a, 0, V-1.0);
            epsV.set(a, 0, epsV.get(a-1, 0) + depsV);
            epsD.set(a, 0, epsD.get(a-1, 0) + depsD);



            if (yield < 0)
                yield = Math.pow(q.get(a, 0), 2.0) + Math.pow(M, 2.0)*Math.pow(p.get(a, 0), 2.0) - Math.pow(M, 2.0)*p.get(a, 0)*pc;
            else 
                yield = 0;

        }
    
        pNCL = new Matrix((int)pc - 1, 1);        
        for (int i = 0; i < pNCL.getRowDimension(); i++)
            pNCL.set(i, 0, i);
        qNCL = pNCL.times(M);
        eNCL = matPlus(matPlus((matLog(pNCL).times(l)).uminus(), (N)), -1);
    
        //Critical State Line (CSL)
        pCSL = pNCL.copy();
        Gamma = 1.0+voidy.get(a, 0)+l*Math.log(p.get(a, 0));
        eCSL = matPlus(matPlus((matLog(pCSL).times(l)).uminus(), (Gamma)), -1);
    
        //Final Yield Surface
        p_fyield = new Matrix((int)pc - 1, 1); 
        for (int i = 0; i < pNCL.getRowDimension(); i++)
            p_fyield.set(i, 0, i);
        q_fyield = p_fyield.times(M);
        qyf = matPow(((p_fyield.times(pc).minus(matPow(p_fyield, 2))).times(Math.pow(M, 2))), 0.5);
        
    }
    
    //Run with all data
	TriaxialTest(double cpi, double p0i, double Mi, double li, double ki, double Ni, double nui, int ana, int iteration, double strsteps) {
        cpiu = cpi;
        p0iu = p0i;
        Miu = Mi;
        liu = li;
        kiu = ki;
        Niu = Ni;
        nuiu = nui;
        strstepsi = strsteps;
        
        cp = cpi;
        p0 = p0i;
        M = Mi;
        l = li;
        k = ki;
        N = Ni;
        nu = nui;
        analysis = ana;
        
        //Computation of Other Parameters (V,e0 and OCR)
        pc = cp;
        V = N - (l * Math.log(pc)) + (k * Math.log(pc/p0));
        e0 = V - 1;
        OCR = cp / p0;

        
        
        //Strain Increament and Strain Matrix Definition
        if (iteration < 7500)
            iter = 7500;
        else
            iter=iteration;
        
        if (strsteps > 0.01 || strsteps <= 0.)
            ide=0.01;
        else 
            ide=strsteps;
        
        de = ide/100;
        
        es = new Matrix(1, iter);
        
        double ic = 0;
        for (int i = 0; i < iter; ic = ic + ide, i++)
            es.set(0, i, ic);
        
        
        
        //Block memory
        De = new Matrix(6, 6);
        dfds = new Matrix(6, 1);
        dfdep = new Matrix(6, 1);
        u = new Matrix(iter, 1);
        p = new Matrix(iter, 1);
        q = new Matrix(iter, 1);
        dStrain = new Matrix(6, 1);
        voidy = new Matrix(iter, 1);
        epsV = new Matrix(iter, 1);
        epsD = new Matrix(iter, 1);

        
        
        // Yield Surface and Conditions
        p_ini_yield = new Matrix((int)pc, 1);       
        for (int i = 0; i < (int)pc; i++)
            p_ini_yield.set(i, 0, i);
        q_ini_yield = p_ini_yield.times(M);
        //Plot the initial yield locus
        qy = matPow(((p_ini_yield.times(pc)).minus(matPow(p_ini_yield,2))).times(Math.pow(M, 2)), 0.5);

        
        //initialise
        int a = 0; //iterator
        //Stress
        double[] nin = {p0, p0, p0, 0, 0, 0};
        S = new Matrix(nin, 1);
        //Strain
        double[] bin = {0,0,0,0,0,0};
        strain = new Matrix(bin, 1);
        

        p.set(0, a, (S.get(0,0) + 2*S.get(0,2)) / 3);
        q.set(0, a, S.get(0,0) - S.get(0,2));
        yield = (Math.pow(q.get(0, a), 2) / Math.pow(M, 2) + Math.pow(p.get(0, a),2)) - p.get(0, a)*pc; // Defining the yield surface
        voidy.set(0, a, e0);
        

        
        
        
        //CamClay Iteration Uni-Loop Iteration for OC/NC & Inside/Outside Yield
        while (a < iter - 1) {
            K=V*p.get(a, 0)/k;                     // Bulk Modulus
            G=(3*K*(1-2*nu))/(2*(1+nu));      // Shear Modulus
            
            if (yield == 0)
                pc = (Math.pow(q.get(a, 0), 2.0)/Math.pow(M, 2.0) + Math.pow(p.get(a, 0), 2.0))/p.get(a, 0);
            else 
                pc = cp;

            //Elastic Stiffness and other Matrices
            for (int m = 0; m < 6; m++) {
                for (int n = 0; n < 6; n++) {
                    if (m <= 2) {
                        if (yield < 0) {
                            dfds.set(m, 0, 0);
                            dfdep.set(m, 0, 0);
                        } else {
                            dfds.set(m, 0, (2.0*p.get(a, 0)-pc)/3.0 + 3.*(S.get(0, m)-p.get(a, 0))/Math.pow(M,2));
                            dfdep.set(m, 0, (-p.get(a, 0))*pc*(1.0 + e0)/(l-k)*1.0);
                        }
                        if (m == n) {
                            De.set(n, m, K+4.0/3.0*G); //Elastic Stiffness
                        } else if (n<=2) {
                            De.set(n, m, K-2.0/3.0*G);
                        }
                    }
                    if (m>2) {
                        dfds.set(m, 0, 0);
                        dfdep.set(m, 0, 0); //%df/ds' and %df/dep
                       
                        if (m==n) {
                            De.set(n, m, G);  //Elastic Stiffness
                        } else  
                            De.set(n, m, 0);
                    }
                }
            }


           //Stiffness Matrix 
           if (yield<0) {
               D=De; //Elastic
           } else 
               D = De.minus((De.times(dfds.times((dfds.transpose()).times(De)))).times(1/(((dfds.transpose()).times(De)).times(dfds).minus(dfdep.transpose().times(dfds))).getArrayCopy()[0][0]));


           //Stress and Strain Updates
            if (analysis==1) { //Triaxial Drained
                double[] tempStrain = {de, -1.0*D.get(1, 0)/(D.get(1, 1)+D.get(1, 2))*de, -1.0*D.get(2, 0)/(D.get(2, 1)+D.get(2, 2))*de, 0, 0, 0};
                dStrain = new Matrix(tempStrain, 1);
            } else if (analysis==2) { //Triaxial Undrained
                double[] tempStrain = {de, -de/2.0, -de/2.0, 0, 0, 0};
                dStrain = new Matrix(tempStrain, 1);
            } else { //Oedometer Drained (analysis=3)
                double[] tempStrain = {de, 0, 0, 0, 0, 0};
                dStrain = new Matrix(tempStrain, 1);
            }

            dS = dStrain.times(D);
            S=S.plus(dS);
            strain = strain.plus(dStrain);

            depsV = dStrain.get(0, 0) + dStrain.get(0, 1) + dStrain.get(0, 2); // Increamental Volumetric Strain
            depsD = 2.0/3.0 * (dStrain.get(0, 1) - dStrain.get(0, 3));      // Increamental Deviatoric Strain



            //Update Specific Volume
            V=N-(l*Math.log(pc))+(k*Math.log(pc/p.get(a, 0)));


            //Subsequent cycle update
            a = a+1;
            
            p.set(a, 0, (S.get(0, 0) + S.get(0, 1) + S.get(0, 2))/3);
            q.set(a, 0, S.get(0, 0) - S.get(0, 2));
            u.set(a, 0, p0 + q.get(a, 0)/3.0 - p.get(a, 0));

            voidy.set(a, 0, V-1.0);
            epsV.set(a, 0, epsV.get(a-1, 0) + depsV);
            epsD.set(a, 0, epsD.get(a-1, 0) + depsD);



            if (yield < 0)
                yield = Math.pow(q.get(a, 0), 2.0) + Math.pow(M, 2.0)*Math.pow(p.get(a, 0), 2.0) - Math.pow(M, 2.0)*p.get(a, 0)*pc;
            else 
                yield = 0;

        }
        /*
        System.out.println("K"+K);
        System.out.println("G"+G);
        System.out.println("k"+k);
        System.out.println("V"+V);
        System.out.println("dfds"+Arrays.toString(dfds.getArrayCopy()[0]));
        System.out.println("dfdep"+Arrays.toString(dfdep.getArrayCopy()[0])+""+Arrays.toString(dfdep.getArrayCopy()[1]));
         System.out.println("De"+Arrays.toString(De.getArrayCopy()[0]));
        System.out.println("S"+Arrays.toString(S.getArrayCopy()[0]));
        System.out.println("dS"+Arrays.toString(dS.getArrayCopy()[0]));
        System.out.println("D"+Arrays.toString(D.getArrayCopy()[0]));
        System.out.println("D"+Arrays.toString(D.getArrayCopy()[1]));
        System.out.println("D"+Arrays.toString(D.getArrayCopy()[2]));
        System.out.println("D"+Arrays.toString(D.getArrayCopy()[3]));
        System.out.println("D"+Arrays.toString(D.getArrayCopy()[4]));
        System.out.println("D"+Arrays.toString(D.getArrayCopy()[5]));
        System.out.println("dStrain"+Arrays.toString(dStrain.getArrayCopy()[0]));
        System.out.println("analysis"+analysis);
        //System.out.println("p"+Arrays.toString(p));
        System.out.println("pc"+pc);*/
    
        //Normal Consolidation Line (NCL)
        pNCL = new Matrix((int)pc - 1, 1);        
        for (int i = 0; i < pNCL.getRowDimension(); i++)
            pNCL.set(i, 0, i);
        qNCL = pNCL.times(M);
        eNCL = matPlus(matPlus((matLog(pNCL).times(l)).uminus(), (N)), -1);
    
        //Critical State Line (CSL)
        pCSL = pNCL.copy();
        Gamma = 1.0+voidy.get(a, 0)+l*Math.log(p.get(a, 0));
        eCSL = matPlus(matPlus((matLog(pCSL).times(l)).uminus(), (Gamma)), -1);
    
        //Final Yield Surface
        p_fyield = new Matrix((int)pc - 1, 1); 
        for (int i = 0; i < pNCL.getRowDimension(); i++)
            p_fyield.set(i, 0, i);
        q_fyield = p_fyield.times(M);
        qyf = matPow(((p_fyield.times(pc).minus(matPow(p_fyield, 2))).times(Math.pow(M, 2))), 0.5);
        
    }
    
	/*
    //Over Consolidation Ratio
    int OCR() {
        if OCR<=1 
       out7=fprintf('\n Soil is Normally Consolidated with a OCR of = %d \n',OCR);
        else
       out8=fprintf('\n Soil is Over Consolidated with a OCR of = %d \n',OCR);
    }*/
	
    
    //Return data types
    double[][] getes() {
        return es.getArrayCopy();
    }
    
    double[][] getq() {
        return q.getArrayCopy();
    }
    
    double[][] getu() {
        return u.getArrayCopy();
    }
    
    double[][] getepsV() {
        return epsV.getArrayCopy();
    }
    
    double getOCR() {
        return OCR;
    }
    
    double[][] getp() {
        return p.getArrayCopy();
    }
    
    double[][] getvoidy() {
        return voidy.getArrayCopy();
    }
    
    int getiter() {
        return iter;
    }
    
    int getanalysis() {
        return analysis;
    }
    
    String getSample() {
        return Miu + "," + liu + "," + kiu + "," + Niu + "," + nuiu; 
    }
    
    String getSampleExplan() {
    	String sampleData;
    	if (cpiu == -1)
    		sampleData = "<html>No sample loaded.<br></html>" ;
    	else
    		sampleData = "<html>Critical Friction Angle M			:" + Miu 
            		+ "<br>Lamda								:" + liu 
            		+ "<br>Kappa								:" + kiu 
            		+ "<br>N									:" + Niu 
            		+ "<br>poissons ratio						:" + nuiu +"<br>";
    	return sampleData;
    }
    
    public String sampleToString() {
    	return name + "," + Miu + "," + liu + "," + kiu + "," + Niu + "," + nuiu; 
    }
    
    public String toString() {
        return name + "," + cpiu + "," + p0iu + "," + Miu + "," + liu + "," + kiu + "," + Niu + "," + nuiu; 
    }
    
    String getTest() {
        if (analysis == 1)
            return cpiu + "," + p0iu + "," + Miu + "," + liu + "," + kiu + "," + Niu + "," + nuiu + ",Drained" + "," + iter + "," + strstepsi;
        else
            return cpiu + "," + p0iu + "," + Miu + "," + liu + "," + kiu + "," + Niu + "," + nuiu + ",Undrained" + "," + iter + "," + strstepsi;
    }
}