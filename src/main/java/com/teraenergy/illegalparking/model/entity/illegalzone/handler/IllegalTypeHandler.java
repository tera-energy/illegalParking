package com.teraenergy.illegalparking.model.entity.illegalzone.handler;

import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date : 2022-09-25
 * Author : young
 * Project : illegalParking
 * Description :
 */
public class IllegalTypeHandler <E extends Enum<E>> implements TypeHandler<IllegalType> {
    private Class<E> type;

    public IllegalTypeHandler(Class<E> type){
        this.type = type;
    }


    @Override
    public void setParameter(PreparedStatement ps, int i, IllegalType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public IllegalType getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return getIllegalType(value);
    }

    @Override
    public IllegalType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getIllegalType(rs.getString(columnIndex));
    }

    @Override
    public IllegalType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getIllegalType(cs.getString(columnIndex));
    }

    private IllegalType getIllegalType(String value) throws TypeException {
        try {
            IllegalType[] enumConstants = (IllegalType[]) type.getEnumConstants();
            for ( IllegalType illegalType: enumConstants) {
                if (illegalType.getValue() == value) {
                    return illegalType;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TypeException("Can't make enum object '" + type + "'", e);
        }
    }
}
