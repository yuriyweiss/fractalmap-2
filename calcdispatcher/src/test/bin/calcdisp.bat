rem cd C:\Users\yuriy\work\personal-projects\fractalmap-2\calcdispatcher\src\test
set BASE_DIR=C:\Users\yuriy\work\personal-projects\fractalmap-2\calcdispatcher\src\test
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_221
cd /D %BASE_DIR%
"%JAVA_HOME%\bin\java" -version

"%JAVA_HOME%\bin\java" -Xmx512M -classpath %BASE_DIR%\lib\* org.fractal.map.launcher.CalcDispatcher > %BASE_DIR%\logs\calcdisp.out
rem "%JAVA_HOME%\bin\java" -Xmx512M -classpath %BASE_DIR%\lib\* org.fractal.map.tests.load.LoadGenerator > %BASE_DIR%\logs\calcdisp.out
rem "%JAVA_HOME%\bin\java" -Xmx512M -classpath %BASE_DIR%\lib\* org.fractal.map.tests.load.compare.SquaresComparator > %BASE_DIR%\logs\calcdisp.out