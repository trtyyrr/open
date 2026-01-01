#!/system/bin/sh
# 目录标记：/app/src/main/assets/scripts/mount_engine.sh

MOD_DIR="/data/adb/stealth/modules"
WORK_DIR="/data/adb/stealth/workdir"

# 1. 确保工作目录干净（OverlayFS 必须）
rm -rf $WORK_DIR/*

# 2. 遍历模块目录
ls $MOD_DIR | while read mod; do
    # 如果存在 'disable' 文件则跳过
    if [ -f "$MOD_DIR/$mod/disable" ]; then
        continue
    fi

    # 3. 执行 OverlayFS 挂载逻辑
    if [ -d "$MOD_DIR/$mod/system" ]; then
        mkdir -p "$WORK_DIR/$mod"
        # lowerdir: 原系统(只读)
        # upperdir: 模块文件(读写)
        # workdir: 内核所需的索引目录
        mount -t overlay "stealth_mod_$mod" \
              -o "lowerdir=/system,upperdir=$MOD_BASE/$mod/system,workdir=$WORK_BASE/$mod" \
              /system
    fi
    
    # 4. 执行模块自带的初始化脚本
    if [ -f "$MOD_DIR/$mod/post-fs-data.sh" ]; then
        sh "$MOD_DIR/$mod/post-fs-data.sh"
    fi
done