def bubble_sort(arr):
    """
    冒泡排序：对列表进行升序排序。
    :param arr: 可比较元素组成的列表（元素之间可比较，例如数字或字符串）
    :return: 排序后的同一个列表对象
    """
    n = len(arr)
    for i in range(n - 1):  # 外层循环控制排序的轮数，每一轮确定一个最大（剩余无序区最大）元素
        swapped = False     # 设置标志位，判断本轮是否有元素交换，优化提前终止排序
        for j in range(0, n - 1 - i):  # 内层循环，两两比较相邻元素，未排序部分逐步减少
            if arr[j] > arr[j + 1]:    # 如果前一个元素大于后一个元素，则交换两者
                arr[j], arr[j + 1] = arr[j + 1], arr[j]  # 进行交换
                swapped = True         # 发生交换后将标志位置为True
        if not swapped:                # 如果本轮没发生交换，说明已经有序
            break                      # 直接结束循环，排序提前完成
    return arr



if __name__ == "__main__":
    # 示例数据，你可以自行修改
    nums = ["张三", "李四", "王五", "赵六", "孙七", "周八", "吴九", "郑十", "陈十一", "林十二", "何十三", "马十四", "梁十五", "沈十六", "王十七"]
    print("原列表:", nums)
    sorted_nums = bubble_sort(nums)
    print("排序后:", sorted_nums)

