
CPU_ARCH=mips64el
CPU_ARCH=loongarch64

export JAVA_HOME=${HOME}/Jogamp-2.4.0/jdk-17.0.5-${CPU_ARCH}

JDK_RT_PATH=${JAVA_HOME}/lib/rt.jar

if [ -d build ]; then
    echo
    # rm -rf build
    # rm -rf build-temp
fi

cd make
ant \
    -Dtarget.sourcelevel=1.8 \
    -Dtarget.targetlevel=1.8 \
    -Dtarget.rt.jar=${JDK_RT_PATH}


cd ..
pwd

LIB_DIR=../libs-${CPU_ARCH}
if [ ! -d ${LIB_DIR} ]; then
    mkdir ${LIB_DIR}
fi

JOGL_DIR=build/jar
for file in jogl-all.jar \
            jogl-all-natives-linux-${CPU_ARCH}.jar
do
    file=${JOGL_DIR}/${file}
    if [ ! -f ${file} ]; then
        exit
    fi
    cp ${file} ${LIB_DIR}
done
