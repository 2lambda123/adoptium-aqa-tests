# OpenJDK regression tests

## Add a sub group test
Adding a <test></test> in playlist.xml specify:

* testCaseName
* command (how to run the test)
* subset: sdk version
* levels: sanity, extended
* groups: openjdk

## Exclude a testcase
Update ProblemList_(JVM_VERSION).txt to exclude testcases which fails in adoptopenjdk regression test build.

List items  are testnames followed by labels, all MUST BE commented
as to why they are here and use a label:

* generic-all   Problems on all platforms
* generic-ARCH  Where ARCH is one of: x64, s390x, ppc64le, sparc, sparcv9, i586, etc.
* OSNAME-all    Where OSNAME is one of: solaris, linux, windows, macosx, aix
* OSNAME-ARCH   Specific on to one OSNAME and ARCH, e.g. solaris-amd64
* OSNAME-REV    Specific on to one OSNAME and REV, e.g. solaris-5.8

	
**Note:** If the test will be run more than once ( more than one annotation @test in test source code) need to append specific testcase number something like following:

	* java/util/concurrent/tck/JSR166TestCase.java#id0  0000 generic-all

## Fixing the tests:
Some tests just may need to be run with "othervm", and that can easily be
done by adding a @run line (or modifying any existing @run):
	
	* @run main/othervm NameOfMainClass
Make sure this @run follows any use of @library.
Otherwise, if the test is a samevm possibility, make sure the test is
cleaning up after itself, closing all streams, deleting temp files, etc.
Keep in mind that the bug could be in many places, and even different per
platform, it could be a bug in any one of:

* the testcase
* the jdk (jdk classes, native code, or hotspot)
* the native compiler
* the javac compiler
* the OS (depends on what the testcase does)

If you managed to really fix one of these tests, here is how you can
remove tests from this list:

* Make sure test passes on all platforms with samevm, or mark it othervm
* Make sure test passes on all platforms when run with it's entire group
* Make sure both VMs are tested, -server and -client, if possible
* Make sure you try the -d64 option on Solaris
* Use a tool like JPRT or something to verify these results
* Delete lines in this file, include the changes with your test changes

You may need to repeat your testing 2 or even 3 times to verify good
results, some of these samevm failures are not very predictable.
