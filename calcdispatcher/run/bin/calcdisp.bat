rem cd C:\Users\yuriy\work\personal-projects\fractalmap-2\calcdispatcher
set BASE_DIR=C:\Users\yuriy\work\personal-projects\fractalmap-2\calcdispatcher
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_221
cd /D %BASE_DIR%
"%JAVA_HOME%\bin\java" -version
"%JAVA_HOME%\bin\java" -Xmx512M -classpath %BASE_DIR%\lib\* org.fractal.map.launcher.CalcDispatcher > %BASE_DIR%\logs\calcdisp.out