@echo Enter the host:
@set /p ip=
@echo Enter the Decryption Key:
@set /p key=
@"C:\Program Files\Java\jdk-12.0.1\bin\javac.exe" client.java
@"C:\Program Files\Java\jdk-12.0.1\bin\javac.exe" aes.java
@"C:\Program Files\Java\jdk-12.0.1\bin\java.exe" client %ip% %key%