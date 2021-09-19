import java.util.Arrays;
public class DSTester {

    String[] items = new String[7];
    int numItems = 0;
    int index1 = 0, index2 = 0;

    public static void main(String [] args) {
        DSTester ds = new DSTester();
        ds.insert(""+13);
        ds.insert(""+33);
        ds.remove();
        ds.insert(""+4);
        ds.insert(""+11);
        ds.insert(27);
        System.out.println(
        Arrays.toString(ds.items));
    }

    public void insert(String item) {
        index2++;
        if (index2 >= items.length)
            index2 = 0;
            items[index2] = item;
            numItems++;
    }

    public String remove() {
        if (numItems<=0) return null;
        String item = items[index1];
        items[index1] = null;
        index1++;
        if (index1 >= items.length)
            index1 = 0;
        numItems--;
        return item;
    }
}