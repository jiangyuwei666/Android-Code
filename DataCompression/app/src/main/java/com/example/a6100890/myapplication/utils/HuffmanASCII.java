package com.example.a6100890.myapplication.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by a6100890 on 2018/1/4.
 */

public class HuffmanASCII {
    private static final int R = 256;   //ASCII字母表
    private static File trie = new File("trie.txt");

    private static class Node implements Comparable<Node> {
        private char ch;        //叶子节点中需要被编码的字符(内部结点不会使用该变量)
        private int freq;       //展开过程不会使用该变量
        private final Node left, right;

        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(Node o) {
            return this.freq - o.freq;
        }
    }

    /**
     * 使用前缀码展开被编码的比特流
     *
     * @throws IOException
     */
    public static void expand(String filename) throws IOException {
        File file = new File(filename);
        StringBuilder sb = new StringBuilder();
        DataInputStream in = new DataInputStream(new FileInputStream(trie));
        //读取单词数
        int N = in.readInt();
        //读取单词查找树,用这棵树将比特流的其余部分展开
        Node root = readTrie(in);

        in = new DataInputStream(new FileInputStream(file));
        //展开第i个编码所对应的字母
        for (int i = 0; i < N; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                //读取二进制
                if (in.readBoolean())
                    x = x.right;
                else
                    x = x.left;
            }
            //x isLeaf
            //输出该结点的字符并重新回到根节点
            sb.append(x.ch);
        }
        System.out.println(sb.toString());
        in.close();
    }

    /**
     * 使用单词查找树递归构造编译表
     * 遍历整棵树并为每个结点维护了一条从根节点到它的路径所对应的二进制字符串
     *
     * @param st 使用 字符 索引的数组String[] st,数组值是这个字符的比特字符串
     * @param x
     * @param s
     */
    private static void buildCode(String[] st, Node x, String s) {
        if (x.isLeaf()) {
            st[x.ch] = s;
            return;
        }
        buildCode(st, x.left, s + '0');
        buildCode(st, x.right, s + '1');
    }

    private static String[] buildCode(Node root) {
        String[] st = new String[R];
        buildCode(st, root, "");
        return st;
    }

    /**
     * 构造霍夫曼单词查找树
     *
     * @param freq 输入流中的字符在输入流的出现频率,为了得到这些频率,需要读取整个输入流
     * @return
     */
    private static Node buildTrie(int[] freq) {
        //使用多棵单节点树初始化优先队列
        //每棵树表示输入流中的一个字符,freq表示它在输入流的出现频率
        MinPQ<Node> pq = new MinPQ<>();
        //输入流中不存在的字符下标对应的数组值为0
        for (char c = 0; c < R; c++) {
            if (freq[c] > 0)
                pq.insert(new Node(c, freq[c], null, null));
        }

        //自底向上合并两棵频率最小的树构造这棵编码的单词查找树
        while (pq.size() > 1) {
            Node x = pq.delMin();
            Node y = pq.delMin();
            Node parent = new Node('\0', x.freq + y.freq, x, y);
            pq.insert(parent);
        }

        return pq.delMin();
    }

    /**
     * 前序遍历导出解码用的单词查找树
     *
     * @param x
     */
    private static void writeTrie(Node x, DataOutputStream out) throws IOException {
//        如果当前节点是叶子节点,就先输出1,然后输出这个结点的ASCII
        if (x.isLeaf()) {
            out.writeBoolean(true);
            out.writeChar(x.ch);
            return;
        }
//        如果是内部结点,就输出0
        out.writeBoolean(false);
        writeTrie(x.left, out);
        writeTrie(x.right, out);
    }

    /**
     * 从比特流的前序表示中重建单词查找树
     *
     * @return
     */
    private static Node readTrie(DataInputStream in) throws IOException {
        //读取比特流的前缀,如果读取到1,说明接下来几位是该结点的ASCII
        //所以再读取一个char构造一个结点
        if (in.readBoolean()) {
            return new Node(in.readChar(), 0, null, null);
        }
        return new Node('\0', 0, readTrie(in), readTrie(in));
    }

    public static String readFile(File filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String s;
        StringBuilder sb = new StringBuilder();

        while ((s = in.readLine()) != null) {
            sb.append(s + '\n');
        }

        in.close();
        return sb.toString();
    }


    /**
     * 压缩ASCII字符串并输出到指定文件
     *
     * @param s
     * @param outfile
     */
    public static void compress(String s, File outfile) throws IOException {
        char[] input = s.toCharArray();     //字符串转化为字符数组

        //统计频率,新建一个数组保存每个ASCII字符的频率
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            freq[c]++;
        }

        //构造霍夫曼编码树
        Node root = buildTrie(freq);

        //构造编译表,编译表的下标是char,值是对应的二进制字符串
        String[] st = new String[R];
        buildCode(st, root, "");

//        打印解码用的单词查找树
        DataOutputStream out = new DataOutputStream(new FileOutputStream(trie));
//        打印单词总数
        out.writeInt(input.length);
        writeTrie(root, out);

        //打印霍夫曼编码
        //依次遍历每个输入字符
        out = new DataOutputStream(new FileOutputStream(outfile));
        for (int i = 0; i < input.length; i++) {
            //从编译表st中取出二进制字符串
            String code = st[input[i]];
//            out.write(code.getBytes());

            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '1') {
                    out.writeBoolean(true);
                } else {
                    out.writeBoolean(false);
                }
            }
        }
        out.close();
    }
}
