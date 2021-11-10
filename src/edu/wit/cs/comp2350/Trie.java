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
	@Override
	public String[] getSuggestions(String s) {
		ArrayList<String> out = new ArrayList<String>();
		_getSuggestions(out, "", root, 2, s.toCharArray(), 0);
		String[] output = new String[out.size()];
		for(int i = 0; i < output.length; i++)
			output[i] = out.get(i);
		return output;
	}

	private void _getSuggestions(ArrayList<String> out, String string, TrieNode curr, int editDis, char[] word, int currChar) {
		if(currChar < word.length) {
			if(editDis >= 0) {
				for(int i = 0; i < 26; i++) {
					if(curr.getChild(i) != null) {
						int currCharIndex = currChar+1;
						String str = string + curr.getChild(i).getChar();
						if(curr.getChild(i).getChar() == word[currChar]) {
							_getSuggestions(out, str, curr.getChild(i), editDis, word, currCharIndex);
						}else { 
							int eD = editDis-1;
							_getSuggestions(out, str, curr.getChild(i), eD, word, currCharIndex);
						}
					}
				}
			}
		}else {
			if(curr.getEndWord() && editDis >= 0)
				out.add(string);
		}
	}
}