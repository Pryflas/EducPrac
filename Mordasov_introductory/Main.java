import java.util.Scanner;

class StackNode {
    String data;
    StackNode next;

    StackNode(String data) {
        this.data = data;
        this.next = null;
    }
}

class CustomStack {
    private StackNode top;
    private int size;

    public CustomStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(String val) {
        StackNode newNode = new StackNode(val);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public void pop() {
        if (top != null) {
            top = top.next;
            size--;
        }
    }

    public String top() {
        if (top != null) {
            return top.data;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return top == null;
    }
}



public class Main {
    public static boolean isValidHTML(String html) {
        CustomStack stack = new CustomStack();
        int pos = 0;
        while (pos < html.length()) {
            if (html.charAt(pos) == '<') {
                int tagStart = pos + 1;
                int tagEnd = html.indexOf('>', tagStart);
                if (tagEnd != -1) {
                    String tag = html.substring(tagStart, tagEnd);
                    if (tag.charAt(0) == '/') {
                        if (!stack.empty()) {
                            String topTag = stack.top();
                            if (topTag.equals(tag.substring(1))) {
                                stack.pop();
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else if (!tag.equals("br") && !tag.equals("hr")) {
                        stack.push(tag);
                    }
                    pos = tagEnd + 1;
                } else {
                    return false;
                }
            } else {
                pos++;
            }
        }
        return stack.empty();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String html = scanner.nextLine();
        boolean isValid = isValidHTML(html);
        if (isValid) {
            System.out.println("correct");
        } else {
            System.out.println("wrong");
        }
        scanner.close();
    }
}
