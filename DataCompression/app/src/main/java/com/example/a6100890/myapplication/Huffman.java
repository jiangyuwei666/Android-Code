package com.example.a6100890.myapplication;

import android.util.Log;

import com.example.a6100890.myapplication.model.Data;
import com.example.a6100890.myapplication.model.EncodeResult;
import com.example.a6100890.myapplication.model.Node;
import com.example.a6100890.myapplication.utils.MinPQ;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by a6100890 on 2018/1/4.
 */

public class Huffman {
    private static final String TAG = "Huffman";

    private Node createTree(MinPQ<Node> letterPQ) {
        while (letterPQ.size() > 1) {
            Node nodeLeft = letterPQ.delMin();
            Node nodeRight = letterPQ.delMin();
            Node nodeParent = new Node();
            nodeParent.setLeftChild(nodeLeft);
            nodeParent.setRightChild(nodeRight);
            Data data = new Data();
            data.setFrequency(nodeRight.getData().getFrequency() + nodeLeft.getData().getFrequency());
            nodeParent.setData(data);

            letterPQ.insert(nodeParent);
        }

        Node rootNode = letterPQ.delMin();
        return rootNode;
    }


    private MinPQ<Node> toPQ(String letters) {
        MinPQ<Node> letterPQ = new MinPQ<>();
        //字符-频率对
        Map<Character, Integer> ci = new HashMap<>();

        //遍历目标字符串.统计每个字符出现的频率
        for (int i = 0; i < letters.length(); i++) {
            Character character = letters.charAt(i);
            if (!ci.keySet().contains(character)) {
                ci.put(character, 1);
            } else {
                Integer oldValue = ci.get(character);
                ci.put(character, oldValue + 1);
            }
        }

        for (Character key : ci.keySet()) {
            Node node = new Node();
            Data data = new Data();
            data.setC(key);
            data.setFrequency(ci.get(key));
            node.setData(data);

            letterPQ.insert(node);
        }

        return letterPQ;
    }


    /**
     * 获取所有字符的二进制编码字符串
     *
     * @param rootNode 哈夫曼树的根节点
     * @return 字符-编码对
     */
    private Map<Character, String> getLetterCode(Node rootNode) {
        Map<Character, String> letterCode = new HashMap<>();

        //如果只有一个结点,就不必递归遍历
        if (rootNode.getLeftChild() == null && rootNode.getRightChild() == null) {
            letterCode.put(rootNode.getData().getC(), "1");
            return letterCode;
        }

        getLetterCode(rootNode, "", letterCode);
        return letterCode;
    }

    /**
     * 前序遍历哈夫曼树,获取所有的字符编码对
     *
     * @param rootNode
     * @param suffix     当前字符的编码前缀
     * @param letterCode 字符的编码结果
     */
    private void getLetterCode(Node rootNode, String suffix, Map<Character, String> letterCode) {
        if (rootNode != null) {
            //如果到达了叶子结点,就取出叶子节点的值,更新键值对
            if (rootNode.getRightChild() == null && rootNode.getLeftChild() == null) {
                Character character = rootNode.getData().getC();
                letterCode.put(character, suffix);
            }

            getLetterCode(rootNode.getLeftChild(), suffix + "0", letterCode);
            getLetterCode(rootNode.getRightChild(), suffix + "1", letterCode);
        }
    }

    /**
     * 读取文件并压缩
     *
     * @param inFilename
     */
    public EncodeResult encode(String inFilename, String decoderFilename) throws IOException {
        BufferedReader in = null;
        DataOutputStream out = null;
        ObjectOutputStream objectOut = null;
        String resultString = null;
        EncodeResult encodeResult = null;
        try {
            File inFile = new File(inFilename);
            String outFilename = inFilename.substring(0, inFilename.lastIndexOf('/') + 1) + "encodeResult.txt";
            File outFile = new File(outFilename);
            Log.d(TAG, "encode: outFile: " + outFile.getAbsolutePath());
            File decoderFile = new File(decoderFilename);
            in = new BufferedReader(new FileReader(inFile));
            out = new DataOutputStream(new FileOutputStream(outFile));

            //读入文件字符串
            StringBuilder inString = new StringBuilder();
            String s;
             while ((s = in.readLine()) != null) {
                inString.append(s + '\n');
            }


            //写二进制编码到outFile
            encodeResult = encode(inString.toString());
            resultString = encodeResult.getEncode();
            Log.d(TAG, "encode: 读取的文件: " + inString.toString());
            Log.d(TAG, "encode: 编码后的文件: " + resultString);


            for (int i = 0; i < resultString.length(); i++) {
                if (resultString.charAt(i) == '1') {
                    out.writeBoolean(true);
                } else {
                    out.writeBoolean(false);
                }
            }

            //写decoder
            objectOut = new ObjectOutputStream(new FileOutputStream(decoderFile));
            //二进制编码-字符对
            Map<String, Character> decoder = getDecoder(encodeResult.getLetterCode());
            objectOut.writeObject(decoder);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (objectOut != null) {
                objectOut.close();
            }
            return encodeResult;
        }

    }


    public EncodeResult encode(String str) {
        //目标字符串转化为Node结点链表
        //结点链表创建哈夫曼树
        MinPQ<Node> letterPQ = toPQ(str);
//        ArrayList<Node> letterList = toList(str);
        Node rootNode = createTree(letterPQ);

        //获取字符串中所有字符的二进制编码字符串
        //根据编码表对目标字符串编码
        Map<Character, String> letterCode = getLetterCode(rootNode);
        EncodeResult result = encode(letterCode, str);      //递归

        return result;
    }

    /**
     * 编码字符串
     *
     * @param letterCode 字符-二进制编码字符串对集合
     * @param letters    指定的需要编码的目标字符串
     * @return 编码结果
     */
    private EncodeResult encode(Map<Character, String> letterCode, String letters) {
        StringBuilder encode = new StringBuilder();

        //遍历目标字符串,取出目标字符串中当前字符对应的二进制字符串
        //然后添加到StringBuilder中按照字符串中的字符顺序组合在一起
        for (int i = 0; i < letters.length(); i++) {
            Character character = letters.charAt(i);
            encode.append(letterCode.get(character));
        }

        return new EncodeResult(encode.toString(), letterCode);
    }

    /**
     * 解码器: 通过字符/编码对 得到 编码/字符对
     * 反转Map的Key 和 Value
     *
     * @param letterCode
     * @return
     */
    private Map<String, Character> getDecoder(Map<Character, String> letterCode) {
        Map<String, Character> decodeMap = new HashMap<>();
        for (Character key : letterCode.keySet()) {
            String value = letterCode.get(key);
            decodeMap.put(value, key);
        }

        return decodeMap;
    }


    /**
     * 解码
     *
     * @param encodeResult 字符串的编码结果
     * @return
     */
    public String decode(EncodeResult encodeResult) {
        //解码得到的字符串
        StringBuffer decodeStr = new StringBuffer();
        //编码-字符对,解码器
        Map<String, Character> decodeMap = getDecoder(encodeResult.getLetterCode());
        //解码器keys 编码的集合
        Set<String> keys = decodeMap.keySet();
        //待解码的二进制字符串
        String encodeString = encodeResult.getEncode();

        //从最短前缀开始匹配
        String temp;
        int i = 1;
        while (encodeString.length() > 0) {
            temp = encodeString.substring(0, i);
            //如果和某一字符的编码匹配成功
            if (keys.contains(temp)) {
                //从编码-字符对中取出这个字符,添加到解码结果中
                Character character = decodeMap.get(temp);
                decodeStr.append(character);

                //把前面解码过的字符串剪掉(从i到末尾)
                encodeString = encodeString.substring(i);
                i = 1;
            } else {
                i++;
            }
        }

        return decodeStr.toString();
    }

    /**
     *
     * @param codeFilename  二进制字符串文件
     * @param decoderFilename   decoder文件
     * @return
     * @throws IOException
     */
    public String decode(String codeFilename, String decoderFilename) throws IOException {
        DataInputStream in = null;
        DataOutputStream out = null;
        ObjectInputStream objectInput = null;
        File codeFile = new File(codeFilename);
        String result = null;
        try {
            //读取编码后的二进制字符串
            StringBuilder code = new StringBuilder();
            in = new DataInputStream(new FileInputStream(codeFile));
            while (in.available() != 0) {
                if (in.readBoolean()) {
                    code.append("1");
                } else {
                    code.append("0");
                }
            }

            //读取构造编码-字符对
            objectInput = new ObjectInputStream(new FileInputStream(decoderFilename));
            Map<String, Character> decoder = (Map<String, Character>) objectInput.readObject();
            //解码后得到的字符串
            StringBuffer decodeStr = new StringBuffer();

            //从最短前缀开始匹配
            Set<String> keys = decoder.keySet();
            String encodeString = code.toString();
            String temp;
            int i = 1;
            while (encodeString.length() > 0) {
                temp = encodeString.substring(0, i);
                //如果和某一字符匹配成功
                if (keys.contains(temp)) {
                    //从编码-字符对中取出这个字符添加到编码结果中
                    Character character = decoder.get(temp);
                    decodeStr.append(character);

                    //把前面解码过的字符串剪掉(从i到末尾)
                    encodeString = encodeString.substring(i);
                    i = 1;
                } else {
                    i++;
                }
            }

            String outFilename = codeFilename.substring(0, codeFilename.lastIndexOf('/') + 1) + "decodeResult.txt";
            out = new DataOutputStream(new FileOutputStream(outFilename));
            result = decodeStr.toString();
            out.write(result.getBytes());

            Log.d(TAG, "decode: 读取到的结果: " + code.toString());
            Log.d(TAG, "decode: 解码后的结果: " + result);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
            objectInput.close();
            return result;
        }
    }
}

