@echo Enter The Encryption Key:
@set /p key=
@"C:\Program Files\Java\jdk-12.0.1\bin\javac.exe" main.java
@"C:\Program Files\Java\jdk-12.0.1\bin\javac.exe" aes.java
@"C:\Program Files\Java\jdk-12.0.1\bin\java.exe" main %key%