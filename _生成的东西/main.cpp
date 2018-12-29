#include <iostream>
#include <string>
#ifndef PI
#define PI = 3.1415926
#endif

using namespace std;

	/*
	 * Sample multiline comment.
	 *
	 * -------Inline test: will it be hightlighted?-------
	 *
	 * Const test : 365984
	 * String test : "Foobar"
	 * Keyword test : if, for, try, catch
	 * Pre-Proessor test : #define, #include
	 * 中文测试
	 */

int main() { // Sample single line comment.
	cout << "Hello world!" << endl;
	cout << "Second line!" << endl;

	int i = -979987;
	i = +123654;
	i = 0X321;
	i = 0x123;
	i = 78u;
	i = 67.23E3;
	i = 67.23E+3;
	float f = 67.23E-3;
//	int i = 3.1415926e;
	i = 3.1415926e7;
	f = 3.1415926f;
	long l = 31415926L;
	l = 356789ul;
	i = 3.26542301123;
	int ary[] = { 1, 2, 3, 4, 5, 666};
	
	return 0;
}

