import sys
import os

import mysql.connector
file = sys.argv[1]
tmp = file.split('.')
tb = tmp[0]
# 打开数据库连接
conn = mysql.connector.connect(host='', user='', password='', database='db_')
# 使用cursor()方法获取操作游标 
cursor = conn.cursor() 
# 使用execute方法执行SQL语句 

def insert(fields,values):
    sql = 'INSERT INTO '+tb +'('
    fds =  ','.join(fields)
    sql = sql + fds + ')'
    vls = ', '.join("'{0}'".format(v) for v in values)
    sql = sql + ' VALUES(' +vls
    dat = len(fields) - len(values) 
    #print(dat)
    if dat > 0:
        fixValues = ['']*dat
        fixV = ', '.join("'{0}'".format(v) for v in fixValues)
        sql = sql + fixV
    sql = sql + ")"
    try:
        cursor.execute(sql)
        cursor.execute('commit')
    except Exception as e:
        print(sql)
i = 0
fields = []
for line in open(file):
    if i == 0:
        fields = line.split()
        print(fields)
        i+=1
        continue
    values = line.split('\t')
    insert(fields,values)
    i+=1 
