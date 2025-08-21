public class BytecodeExplorer {
    private static final String CONSTANT = "Hello";
    private int counter = 0;

    public static void main(String[] args) {
        BytecodeExplorer explorer = new BytecodeExplorer();
        String result = explorer.process("World");
        System.out.println(result);
    }

    public String process(String input) {
        counter++;
        return CONSTANT+" "+input+" (count: "+counter+")";
    }

    public void compareCondition(int x) {
        if(x>10) {
            System.out.println("Greater");
        } else {
            System.out.print("Lesser");
        }
    }
}