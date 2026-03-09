package com.harts.bank.typehandlers;

import com.harts.bank.model.Address;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({Address.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class AddressTypeHandler extends BaseTypeHandler<Address> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Address parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Address getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String addressStr = rs.getString(columnName);
        return addressStr != null && !addressStr.isEmpty() ? Address.fromString(addressStr) : null;
    }

    @Override
    public Address getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String addressStr = rs.getString(columnIndex);
        return addressStr != null && !addressStr.isEmpty() ? Address.fromString(addressStr) : null;
    }

    @Override
    public Address getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String addressStr = cs.getString(columnIndex);
        return addressStr != null && !addressStr.isEmpty() ? Address.fromString(addressStr) : null;
    }
}
