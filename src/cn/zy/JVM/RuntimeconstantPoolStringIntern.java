/**
 * 
 */
package cn.zy.JVM;

/**
 * 
 * @author zy
 * 2017��5��8��
 */
public class RuntimeconstantPoolStringIntern {

	/**
	 * @param args
	 * @return void
	 * @author zy
	 * @date 2017��5��8��
	 */
	public static void main(String[] args) {
		String str1 = new StringBuilder("�����").append("����").toString();
		System.out.println(str1.intern()==str1);

		String str2 = new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern() ==str2);
	}

}