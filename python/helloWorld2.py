#!/usr/bin/env python
import sys

def sayHelloTo(name, exclaim):

	result = "Hello " + name

	if exclaim:
		result += "!!!"

	return result



def main() :
	print sayHelloTo(sys.argv[1], False)
	print sayHelloTo(sys.argv[1], True)


if __name__ == '__main__':
	main()