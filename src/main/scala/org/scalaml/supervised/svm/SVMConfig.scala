/**
 * Copyright 2013, 2014  by Patrick Nicolas - Scala for Machine Learning - All rights reserved
 *
 * The source code in this file is provided by the author for the sole purpose of illustrating the 
 * concepts and algorithms presented in "Scala for Machine Learning" ISBN: 978-1-783355-874-2 Packt Publishing.
 * Unless required by applicable law or agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * Version 0.92
 */
package org.scalaml.supervised.svm

import libsvm._
import org.scalaml.supervised.Config


	/**
	 * <p>Generic stateuration manager for any category of SVM algorithm. The stateuration of a SVM has
	 * three elements:<br>
	 * - Type and parameters of the formulation of the SVM algorithm<br>
	 * - Type and parameter(s) of the Kernel function used for non-linear problems<br>
	 * - Execution (training) stateuration parameters.</p>
	 * @param formulation formulation of the SVM problem
	 * @param kernel kernel function used for non-separable training sets
	 * @param svmParms parameters for the training of the SVM model
	 * 
	 * @author Patrick Nicolas
	 * @since April 30, 2014
	 * @note Scala for Machine Learning
	 */
final protected class SVMConfig(val formulation: SVMFormulation, val kernel: SVMKernel, val svmParams: SVMExecution) extends Config {
	validate(formulation, kernel, svmParams)
	
    val  param = new svm_parameter
    formulation.state(param)
    kernel.state(param)
    svmParams.state(param)
    
    override def toString: String = {
       val buf = new StringBuilder("\nSVM Formulation: ")
       buf.append(param.svm_type)
    	     .append("\ngamma: ")
    	       .append(param.gamma)
    	         .append("\nProbability: ")
    	           .append(param.probability)
    	             .append("\nWeights: ")
    	             
       if( param.weight != null) {
         for( w <- param.weight)
      	   buf.append(w).append(",")
       }
       else 
      	  buf.append(" -no weight")
      buf.toString
    }
    
    @inline def eps: Double = svmParams.eps
    
    @inline
    def isCrossValidation: Boolean = svmParams.nFolds > 0
    
    private def validate(formulation: SVMFormulation, kernel: SVMKernel, svmParams: SVMExecution): Unit =  {
		require(formulation != null, "Formulation in the stateuration of SVM is undefined")
		require(kernel != null, "Kernel function in the stateuration of SVM is undefined")
		require(svmParams != null, "The training execution parameters in the stateuration of SVM is undefined")	
	}
}


		/**
		 * <p>Companion object for SVM stateuration manager used for defining the constructors of SVMConfig class.</p>
		 */
object SVMConfig {
   final val DEFAULT_CACHE = 25000
   final val DEFAULT_EPS = 1e-15
   
   def apply(svmType: SVMFormulation, kernel: SVMKernel, svmParams: SVMExecution): SVMConfig = new SVMConfig(svmType, kernel, svmParams)
   def apply(svmType: SVMFormulation, kernel: SVMKernel): SVMConfig = new SVMConfig(svmType, kernel, new SVMExecution(DEFAULT_CACHE, DEFAULT_EPS, -1))
}

// --------------------------- EOF ------------------------------------------