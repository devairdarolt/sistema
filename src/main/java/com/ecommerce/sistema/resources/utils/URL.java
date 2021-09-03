package com.ecommerce.sistema.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

	/**
	 * Converte "teste%20string" em "teste string"
	 * @param s
	 * @return
	 */
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	/**
	 * Converte Strings em lista de inteiros
	 * 
	 * @param s
	 * @return
	 */
	public static List<Integer> decodeIntList(String s) {
		String[] vet = s.split(",");
		List<Integer> ints = new ArrayList<>();
		for (String e : vet) {

			ints.add(Integer.parseInt(e));

		}
		return ints;
		// Outra forma de fazer
		// return Arrays.asList(s.split(",")).stream().map(x->
		// Integer.parseInt(x)).collect(Collectors.toList());

	}

}
