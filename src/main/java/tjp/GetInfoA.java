package tjp;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

aspect GetInfoA {

	static final void println(String s) {
		System.out.println(s);
	}

	/*
	 * so that only executions made in the control flow of Demo.go are
	 * intercepted. The control flow from the method go includes the execution
	 * of go itself, so the definition of the around advice includes
	 * !execution(* go()) to exclude it from the set of executions advised.
	 */
	//执行Demo的go方法
	pointcut goCut(): cflow(this(Demo) && execution(void go()));

	pointcut demoExecs(): within(Demo) && execution(* *(..));
	
	Object around(): demoExecs() && !execution(* go()) && goCut(){
		
		println("Intercepted message: "
				+ thisJoinPointStaticPart.getSignature().getName());
		println("in class: "
				+ thisJoinPointStaticPart.getSignature().getDeclaringType()
						.getName());

		printParameters(thisJoinPoint);

		println("Running original method: \n");

		Object result = proceed();

		println("  result: " + result);

		return result;
	}

	@SuppressWarnings("unused")
	static private void printParameters(JoinPoint jp) {

		println("Arguments: ");

		Object[] args = jp.getArgs();

		String[] names = ((CodeSignature) jp.getSignature())
				.getParameterNames();

		@SuppressWarnings("unchecked")
		Class<Object>[] types = ((CodeSignature) jp.getSignature()).getParameterTypes();

		for (int i = 0; i < args.length; i++) {
			println("  " + i + ". " + names[i] + " : " + types[i].getName()
					+ " = " + args[i]);
		}
	}
}