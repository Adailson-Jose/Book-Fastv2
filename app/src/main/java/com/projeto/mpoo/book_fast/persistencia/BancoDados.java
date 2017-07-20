package com.projeto.mpoo.book_fast.persistencia;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.projeto.mpoo.book_fast.dominio.Pessoa;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adailson on 20/07/2017.
 */

public class BancoDados extends SQLiteOpenHelper{
    private static final int VERSAO_BANCO = 1;
    private static final String NOME_BANCO = "bd_biblioteca";

    private static final String TABELA_PESSOA = "bd_biblioteca";
    private static final String COLUNA_CPF = "cpf";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_EMAIL = "email";
    private static final String COLUNA_SENHA = "senha";

    public BancoDados(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY_COLUNA = "CREAT TABLE" + TABELA_PESSOA+"("
                +  COLUNA_CPF + " INTERGE PRIMARY KEY,"
                + COLUNA_NOME + " TEXT," + COLUNA_EMAIL + " TEXT," + COLUNA_SENHA
                + " TEXT)";
        db.execSQL(QUERY_COLUNA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //CRUD A BAIXO

    public void addPessoa(Pessoa pessoa){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(COLUNA_CPF, pessoa.getCpf());
        valores.put(COLUNA_NOME, pessoa.getNome());
        valores.put(COLUNA_EMAIL, pessoa.getEmail());
        valores.put(COLUNA_SENHA, pessoa.getSenha());
        db.insert(TABELA_PESSOA, null, valores);
        db.close();
    }
    public void deletarPessoa(Pessoa pessoa){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABELA_PESSOA, COLUNA_CPF + " = ?", new String[]{String.valueOf(pessoa.getCpf())});
        db.close();

    }
    public Pessoa selecioanarPessoa(int cpf){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA_PESSOA, new String[]{COLUNA_CPF, COLUNA_NOME, COLUNA_EMAIL, COLUNA_SENHA}, COLUNA_CPF + " = ?",
                new String[]{String.valueOf(cpf)},null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Pessoa pessoa = new Pessoa(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return pessoa;
    }
    public void atualizarPessoa(Pessoa pessoa){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(COLUNA_CPF, pessoa.getCpf());
        valores.put(COLUNA_NOME, pessoa.getNome());
        valores.put(COLUNA_EMAIL, pessoa.getEmail());
        valores.put(COLUNA_SENHA, pessoa.getSenha());
        db.update(TABELA_PESSOA, valores, COLUNA_CPF + " = ?",
                new String[]{String.valueOf(pessoa.getCpf())});
        db.close();
    }
    public List<Pessoa> getListaPessoas(){
        List<Pessoa> listaPessoa = new ArrayList<Pessoa>();

        String quary = "SELECT * FROM " + TABELA_PESSOA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(quary,null);
        if (cursor.moveToFirst()){
            do {
                Pessoa pessoa = new Pessoa();
                pessoa.setCpf(Integer.parseInt(cursor.getString(0)));
                pessoa.setNome(cursor.getString(1));
                pessoa.setEmail(cursor.getString(2));
                pessoa.setSenha(cursor.getString(3));

                listaPessoa.add(pessoa);
            }while (cursor.moveToNext());
        }
        return listaPessoa;

    }
}
