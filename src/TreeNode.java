import java.util.ArrayList;
import java.util.List;

class TreeNode {
    String value;
    String lexema;
    List<TreeNode> children;

    public TreeNode(String value, String lexema) {
        this.value = value;
        this.lexema = lexema;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return value;
    }
}
