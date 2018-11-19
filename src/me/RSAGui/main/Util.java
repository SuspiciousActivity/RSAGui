package me.RSAGui.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final Pattern factorDBIDPattern = Pattern.compile("index\\.php\\?id=([0-9]+)");

	private static BigInteger resolveFactorDBID(String id) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL("http://factordb.com/index.php?id=" + id)
					.openConnection();
			BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String s;
			while ((s = read.readLine()) != null) { // hacky way of parsing it
				if (s.startsWith("<input type=\"text\" size=100 name=\"query\" value=")) {
					return new BigInteger(s.substring(48, s.length() - 2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BigInteger[] factorN(BigInteger n) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL("http://factordb.com/index.php?query=" + n.toString())
					.openConnection();
			BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String s;
			while ((s = read.readLine()) != null) {
				if (s.contains("middot")) { // hacky way of parsing it
					if (s.split("index\\.php").length != 4) {
						break;
					}
					BigInteger[] ret = new BigInteger[2];
					Matcher m = factorDBIDPattern.matcher(s);
					m.find();
					m.find();
					ret[0] = resolveFactorDBID(m.group(1)); // p
					m.find();
					ret[1] = resolveFactorDBID(m.group(1)); // q
					if (ret[0] != null && ret[1] != null && ret[0].multiply(ret[1]).equals(n)) { // check if those are
																									// really factors
						return ret;
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BigInteger getPhi(BigInteger p, BigInteger q) {
		BigInteger a = p.subtract(BigInteger.ONE);
		BigInteger b = q.subtract(BigInteger.ONE);
		return a.multiply(b.divide(a.gcd(b)));
	}

	public static BigInteger getD(BigInteger e, BigInteger p, BigInteger q) {
		BigInteger n = p.multiply(q);
		BigInteger phi = getPhi(p, q);
		return e.modInverse(phi);
	}

	public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
		return c.modPow(d, n);
	}

	public static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
		return m.modPow(e, n);
	}

}
