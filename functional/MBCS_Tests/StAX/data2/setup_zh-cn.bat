@echo off
@echo (1) GB18030 G1
@echo    
@echo    ���d�U�@
set TEST_STRING1=����d�U�@

@echo (2) GB18030 G2
@echo    
@echo    ANSIString2
set TEST_STRING2=ANSIString2

@echo (3) Full angle character
@echo    
@echo    ���£�һ���ߣ��ܡ�
set TEST_STRING3=���£�һ���ߣ��ܡ�

@echo (4) Half-angle character
@echo    
@echo    ABCCDE/\!
set TEST_STRING4=ABCCDE/\\!

set TEST_STRING=������£�һ���ߣ��ܡ�
set TEST_STRING_SED=%TEST_STRING%
set TEST_STRINGS=%TEST_STRING1% %TEST_STRING2% %TEST_STRING3% %TEST_STRING4%
set TEST_STRINGS_SED=%TEST_STRINGS%