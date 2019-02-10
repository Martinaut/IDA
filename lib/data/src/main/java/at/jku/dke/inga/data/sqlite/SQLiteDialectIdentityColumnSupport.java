package at.jku.dke.inga.data.sqlite;

import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

/**
 * @link https://github.com/gwenn/sqlite-dialect
 */
public class SQLiteDialectIdentityColumnSupport extends IdentityColumnSupportImpl {

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return false;
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        return "select last_insert_rowid()";
    }

    @Override
    public String getIdentityColumnString(int type) {
        return "integer";
    }
}