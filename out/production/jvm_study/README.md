# JVM 밑바닥까지 파헤치기

## 6장

### 6.3 클래스 파일의 구조

#### 클래스 파일 구조 개념 정리

Java 언어로 작성된 자바파일(\*.java, 소스파일)은 Java 컴파일러에 의해 클래스 파일(\*.class)로 변환되는데,</br>
이 클래스 파일은 클래스 이름, 상위 클래스 정보, 메서드, 필드 정보, 바이트코드(bytecode, JVM 명령어), 상수 풀(Constant Pool) 등의 정보가 담겨져 있다.</br>
--> 클래스 파일에는 JVM이 실행할 수 있도록 JVM의 명령어와 클래스 정보가 담겨져 있다고 이해함.

클래스 파일은 바이너리 형식의, 0과 1의 조합인 이진 데이터로 구성된다.</br>
클래스 파일 안의 이진 데이터(0과 1로 구성된)는 데이터 타입을 갖는데,</br>
그 종류가 두 가지다. 기본 데이터 타입을 표현하는 부호 없는 숫자(unsigned number)와</br>
여러 개의 부호없는 숫자나 또 다른 테이블로 구성된 복합 데이터 타입인 테이블이다.

정리하자면

1. 자바 컴파일러는 자바 소스파일(\*.java)을 자바가상머신(jvm)이 읽을 수 있는 클래스 파일로(\*.class) 변환한다.
2. 클래스 파일은 1과 0으로 구성된 바이너리 형식이며,</br>
   1과 0으로 조합된 데이터는 u1(1바이트), u2, u4 등과 같은 부호없는 숫자 타입으로 표현된다.
3. 그리고 부호없는 숫자 타입의 데이터들을 조합하여 상수 풀, 필드, 메서드 등의 정보를 나타내고,</br>
   이 정보들은 테이블의 형태로 관리된다.</br>
   그래서 클래스 파일 전체는 본질적으로 테이블이라고 하는 것 같다(291p, 테이블 개념설명 중)

#### 클래스 파일 내부 데이터 파헤치기

클래스 파일 구조

```java
ClassFile {
  // 클래스 파일인지 확인하는, 파일타입 식별용,
  // 자바 가상머신이 이 클래스 파일을 읽으려면 4바이트인 매직넘버의 값은 0xCAFEBABE 이어야 함.
  u4    magic;
  u2    minor_version;
  u2    major_version;
  u2    constant_pool_count;
  cp    constant_pool[constant_pool_count-1];
}
```

클래스 파일의 처음 4바이트는 매직 넘버로 시작하는데,</br>
이 매직 넘버는 가상 머신이 허용하는 클래스 파일인지 여부를 빠르게 확인하는 용도로 사용된다고 한다.</br>
클래스 파일의 매직 넘버의 값은 0xCAFEBABE 다.</br>
매직 넘버의 값이 0xCAFEBABE 라면 자바 가상머신이 읽을 수 있는 클래스 파일이라고 인식한다고 이해함.</br>
--> 자바 가상머신이 이 클래스 파일을 읽을 수 있는지 확인하는 용도라고 이해함.</br>
--> 매직넘버는 파일 타입 식별용이다.

매직 넘버 다음의 4바이트는 클래스 파일의 버전 번호라고 함.</br>
5\~6번째 바이트는 minor_version을, 7\~8번째 바이트는 메이저 버전을 의미한다고 한다.</br>
버전 번호에 따라 실행할 수 있는 클래스 파일이 달라지는 것 같다.</br>
실행할 수 있는 클래스 파일이라함은 JDK(java development kit) 버전과 관련있음.</br>
jdk는 자바 언어로 개발하기 위한 도구들의 모음이며,</br>
jdk는 JVM, 표준 라이브러리, 컴파일러(javac), javadoc, jar, javap 등의 도구들의 묶음이다.</br>
jvm은 자기보다 높은 버전의 jvm이 컴파일한 클래스 파일을 실행할 수 없으며,</br>
클래스 파일에 작성된 minor_version, major_version 데이터가 담고 있는 jvm 버전을 보고</br>
현재 jvm이 실행할 수 있는지의 여부를 판단할 수 있게 된다고 한다.

정리하자면
jdk에 포함된 기본적인 클래스 파일들은 내부적으로 매직넘버와 버전 정보를 포함하며,</br>
jvm이 버전 정보를 읽어서 실행할 수 있는 파일인지를 파악한다.</br>
현재 jvm보다 높은 버전에서 컴파일된 클래스 파일이라면 해당 클래스 파일을 실행하지 못한다라고 이해함.

상수풀</br>
버전 번호 다음은 상수 풀 항목이다.</br>
상수 풀에 들어 있는 상수의 수는 고정적이지 않으므로</br>
상수 풀 항목들에 앞서 항목 개수를 알려주는 u2 타입 데이터가 필요하다(295p)</br>
상수 풀 항목의 개수를 셀 때, 0이 아닌 1부터 시작하고,</br>
0이라면 '상수 풀 항목을 참조하지 않음'의 의미를 갖는다.

javap(300p)</br>
JDK의 bin디렉토리에 javap라는 실행파일이 있다(javap.exe)</br>
클래스 파일(\*.class)의 바이트 코드(JVM 명령어) 분석 도구다.</br>

#### 실습(제공받은 클래스를 컴파일하고, javap(바이트 코드 분석 도구)로 분석해보기)

1. 자바 컴파일러한테 BytecodeExplorer.java 파일 컴파일하라고 명령하기

```bash
javac BytecodeExplorer.java
```

2. 컴파일된 클래스 파일의 구조를 파악하기</br>
   javap(컴파일된 .class 파일을 읽어서 사람이 읽을 수 있는 형태로 보여주는 도구)한테</br> BytecodeExplorer.class 파일을 읽고 클래스 파일의 구조를 알려달라 하기

```bash
javap BytecodeExplorer
```

아래와 같은 결과가 터미널에 출력된다.</br>
기본 생성자, 프로그램 시작점이라 알고 있는 main 메서드, process 메서드, compareCondition 메서드가 출력되었다.</br>

```java
public class BytecodeExplorer {
  public BytecodeExplorer();
  public static void main(java.lang.String[]);
  public java.lang.String process(java.lang.String);
  public void compareCondition(int);
}
```

3. javap한테 더 상세하게 바이트 코드 분석해달라고 명령하기</br>

```bash
# javap -v: "지정한 클래스 파일을 사람이 읽을 수 읽는 형태로 변환하여(disassemble)
# 클래스 파일의 내부 구조와 바이트 코드를 포함한 상세 정보를 출력해라"는 명령어
# > : '출력 리디렉션 연산자'로 "출력된 정보를 파일로 저장해라"는 의미다.
javap -v BytecodeExplorer > bytecode.txt
```

javap가 클래스 파일을(BytecodeExplorer.class) 사람이 읽을 수 있는 형태로 변환한 내용을 상세히 살펴보기

```java
Classfile /C:/workspace/jvm_study/BytecodeExplorer.class // 클래스 파일 경로
  Last modified 2025. 8. 22.; size 1334 bytes
  SHA-256 checksum df1ff83f22ea01261ef603610ed07ffa1b7a41d4704414592c1890c6627138e4
  Compiled from "BytecodeExplorer.java" // BytecodeExplorer.java(자바 파일)로부터 변환되었다.
  public class BytecodeExplorer
   major version: 65 // 버전 번호 65는 jdk21을 의미,
   // flags는 클래스의 접근자 및 특성을 나타내는 정보다.
   // 클래스의 접근자: 이 클래스(필드, 메서드)에 누가 접근할 수 있는지를 지정하는 키워드 ex. ACC_PUBLIC --> 이 클래스에 모두 접근이 가능하다.
   // 클래스의 특성: 추상 클래스인지(abstract), 상속 불가 클래스인지(final), 인터페이스인지(interface) 등 클래스의 특징을 정보로 나타낸다. ex. ACC_SUPER: 정확하게 부모클래스의 메서드를 호출할 수 있도록 JVM이 처리하라고 알려주는 역할.
   flags: (0x0021) ACC_PUBLIC, ACC_SUPER
   this_class: #8                          // BytecodeExplorer
   super_class: #2                         // java/lang/Object
   interfaces: 0, fields: 2, methods: 4, attributes: 3



```
