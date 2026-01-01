#!/system/bin/sh
# 目录标记：app/src/main/assets/patch_engine.sh

# $1 是 App 传递过来的内部私有路径 (context.filesDir)
# $2 是待修补的 boot.img 路径
BIN_DIR=$1
BOOT_IMG=$2
MAGISKBOOT="$BIN_DIR/magiskboot"

# 执行二进制修补（1.3.4 方案的核心）
$MAGISKBOOT unpack $BOOT_IMG
# 方案 4：硬件欺骗 (Hex Patch)
$MAGISKBOOT hexpatch kernel "androidboot.verifiedbootstate=orange" "androidboot.verifiedbootstate=green "
# 重新打包
$MAGISKBOOT repack $BOOT_IMG /sdcard/Download/patched_boot.img