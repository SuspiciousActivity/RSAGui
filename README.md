# RSAGui
A small GUI to test out plain RSA
Get the precompiled version from here: https://github.com/SuspiciousActivity/Random-Java-Stuff/blob/master/RSAGui.jar

This program is built on top of this wikipedia article about RSA: https://en.wikipedia.org/wiki/RSA_(cryptosystem)

It's using http://factordb.com/ for factoring 'n' into 'p' and 'q'.

# Using it
It's pretty straight forward.

Just enter some numbers.
You can press enter on most of the text fields to calculate other fields.

You can copy a hex value in format '0xDEADBEEF' and paste it into a field, it gets converted to decimal automatically.

# Example
Enter n = 1337, press enter in n.
p and q are fetched from FactorDB, phi is calculated.

Enter e = 65537, press enter in e.
d is calculated.

Enter m = 42, press enter in m.
The cipher c is calculated. You can also press enter in c to get m.
