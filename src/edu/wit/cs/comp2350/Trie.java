package edu.wit.cs.comp2350;
import java.util.ArrayList;
/** Implements a trie data structure 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Assignment 6
 * 
 */

//An implementation of a Trie data structure
public class Trie implements Speller {
	private TrieNode root = new TrieNode();
	
	//Nodes for data structure
	private class TrieNode{
		private boolean endWord;
		private char c;
		private TrieNode parent;
		private TrieNode[] children = new TrieNode[26]; //Size only relevant for 26 lowercase letters in alphabet
		
		public TrieNode() {
			endWord = false;
			parent = null;
		}
		public TrieNode(boolean eW, char val, TrieNode par) {
			endWord = eW;
			c = val;
			parent = par;
		}
		
		public boolean equals(TrieNode n) {
			if(endWord == n.getEndWord() && c == n.getChar() && parent == n.getParent() && children == n.getChildren())
				return true;
			return false;
		}
		
		//public void setParent(TrieNode n) {parent = n;}
		public TrieNode getParent() {return parent;}
		public void setEndWord(boolean b) {endWord = b;}
		public boolean getEndWord() {return endWord;}
		//public void setChar(char in) {c = in;}
		public char getChar() {return c;}
		public void setChild(int index, TrieNode n) {children[index] = n;}
		public TrieNode getChild(int index) {return children[index];}
		private TrieNode[] getChildren() {return children;};
	}

	//Inserts String s into the Trie
	@Override
	public void insertWord(String s) {
		char[] input = s.toCharArray();
		TrieNode curr = root;
		for(char c : input) {
			int index = c - 'a';
			if(curr.getChild(index) == null)
				curr.setChild(index, new TrieNode(false, c, curr));
			curr = curr.getChild(index);
		}
		curr.setEndWord(true);
	}

	//Returns whether the Trie contains String s
	@Override
	public boolean contains(String s) {
		char[] input = s.toCharArray();
		if(input.length == 0)
			return false;
		TrieNode curr = root;
		for(char c : input) {
			int index = c - 'a';
			if(curr.getChild(index) == null)
				return false;
			curr = curr.getChild(index);
		}
		if(curr.getEndWord())
			return true;
		return false;
	}

	//Returns relevant spelling suggestions as String[] for given String s
	/**
	 * Concept: look at all true TrieNodes at depth s.length() and determine if they meet the edit
	 * requirements by traversing back to the root. If they do, add the suggestion to output
	 */
	@Override
	public String[] getSuggestions(String s) {
		char[] word = s.toCharArray();
		ArrayList<TrieNode> trueNodes = getNodesAt(s.length());
		ArrayList<String> out = new ArrayList<String>();
		int editDis = 2;
		for(int i = 0; i < trueNodes.size(); i++) {
			if(checkRequirements(trueNodes.get(i), editDis, word, word.length-1))
				out.add(getWord(trueNodes.get(i)));
		}
		String[] output = new String[out.size()];
		for(int i = 0; i < output.length; i++)
			output[i] = out.get(i);
		return output;
	}
	//Returns list of all true nodes at a depth/height of h
	private ArrayList<TrieNode> getNodesAt(int h){
		ArrayList<TrieNode> output = new ArrayList<TrieNode>();
		ArrayList<TrieNode> children = new ArrayList<TrieNode>();
		output.add(root);
		for(int i = 0; i < h; i++) {
			for(int k = 0; k < output.size(); k++) {
				for(int j = 0; j < 26; j++) {
					if(output.get(k).getChild(j) != null)
						children.add(output.get(k).getChild(j));
				}
			}
			output = children;
			children = new ArrayList<TrieNode>();
		}
		for(int i = 0; i < output.size(); i++) {
			if(output.get(i).getEndWord())
				children.add(output.get(i));
		}
		output = children;
		return output;
	}
	//Checks if the given node meets the requirements for the given word and edit distance
	private boolean checkRequirements(TrieNode n, int editDis, char[] word, int currChar) {
		if(editDis < 0) {
            return false;
        }else if(currChar < 0) {
            return true;
        }
        if(n.getChar() == word[currChar])
            return checkRequirements(n.getParent(), editDis, word, currChar-1);
        else
            return checkRequirements(n.getParent(), editDis-1, word, currChar-1);
	}
	//Returns the word for a given leaf node
	private String getWord(TrieNode n) {
		TrieNode curr = n;
        String out = "";
        while(!curr.equals(root)) {
            out = curr.getChar() + out;
            curr = curr.getParent();
        }
        return out;
	}
}
