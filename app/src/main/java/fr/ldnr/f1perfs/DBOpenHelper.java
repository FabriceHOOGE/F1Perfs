package fr.ldnr.f1perfs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kévin on 19/04/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    // The database
    private SQLiteDatabase db;

    public static class Constants implements BaseColumns {

        // Nom de la BDD
        public static final String DATABASE_NAME = "f1perfs";

        // Version de la BDD
        public static final int DATABASE_VERSION = 1;

        // Nom de la table
        public static final String TABLE_NAME = "Perfs";

        // Noms des colonnes
        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_EVENT = "event";
        public static final String KEY_COL_TIME = "time";
        public static final String KEY_COL_TRACK = "track";
        public static final String KEY_COL_PILOT = "pilot";
    }

    /**
     * Requête de création de la BDD
     */
    private static final String DATABASE_CREATE = "create table "
            + Constants.TABLE_NAME + "("
            + Constants.KEY_COL_ID + " integer primary key autoincrement, "
            + Constants.KEY_COL_EVENT + " TEXT, "
            + Constants.KEY_COL_TIME + " TEXT, "
            + Constants.KEY_COL_TRACK + " TEXT, "
            + Constants.KEY_COL_PILOT + " TEXT) ";


    /**
     * @param context
     *            = contexte de la classe appelant le constructeur
     */
    public DBOpenHelper(Context context)
    {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // est appelé lors de l'instanciation
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DATABASE_CREATE);
    }

    // est utilisée lors de changement de schema dans la base de données
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Mssg d'informations MAJ de la BDD
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion + " vers la version " + newVersion + ", les anciennes données seront détruites ");
        // Effacement de l'ancienne
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        // Création de la nouvelle
        onCreate(db);
    }


    /**
     * Ouverture BDD *
     *
     * @throws SQLiteException
     */
    private void openDB() throws SQLiteException
    {
        try { db = getWritableDatabase(); }
        catch (SQLiteException ex) { db = getReadableDatabase(); }
    }

    /** Fermeture BDD */
    private void closeDB() {
        db.close();
    }

    /**
     * @param event
     *            = event name to record
     * @param time
     *            = time to record
     * @param track
     *            = track name to record
     * @param pilot
     *            = pilot name to record
     *
     */
    public Boolean insertRecord(String event, String time, String track, String pilot)
    {
        // Ouverture BDD
        openDB();

        // INSERT
        db.execSQL("INSERT INTO "+Constants.TABLE_NAME+" (event, time, track, pilot) VALUES (?,?,?,?)", new Object[]{event, time, track, pilot});

        // SELECT pour tester l'insert
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+Constants.TABLE_NAME+" WHERE event=? AND time=? AND track=? AND PILOT=?",new String[]{event, time, track, pilot});

        Boolean result = false;

        // Vérifie si l'insert à réussi
        if(cursor.moveToFirst()) result = true;

        // Fermeture curseur et BDD
        cursor.close();
        closeDB();

        return result;
    }


    /**
     * @param select
     *            = tableau des colonnes à sélectionner
     * @param where
     *            = tableau des colonnes où chercher
     * @param values
     *            = tableau des valeurs à chercher
     *
     */
    public ArrayList<ArrayList<String>> selectRecord(String[] select, String[] where, String[] values, String orderBy, String limit)
    {
        // Ouverture BDD
        openDB();

        // Déclaration du tableau qui contiendra le résultat du select
        ArrayList<ArrayList<String>> resultQuery = new ArrayList<>();

        // Début de la requête
        String selectQuery = "SELECT ";

        // Pour tous les éléments du tableau select
        for (int i = 0; i < select.length; i++)
        {
            // Construction requête (ajout colonnne à sélectionner)
            selectQuery += select[i];
            // Si plusieurs select ajout d'une virgule sauf pour le dernier
            if(i < select.length - 1) selectQuery += ", ";
            // Ajout d'un tableau pour chaque select dans le tableau résultat
            resultQuery.add(new ArrayList<String>());
        }

        // Construction requête (ajout du FROM nom_de_la_table)
        selectQuery += " FROM " + Constants.TABLE_NAME;

        // si le tableau where contient des valeurs et qu'il y en a autant dans values
        if (where.length > 0 && where.length == values.length)
        {
            // Déclaration du WHERE
            String selectWhere = " WHERE ";
            // Parcours des éléments du tableau where
            for (int i = 0; i < where.length; i++)
            {
                // Construction requête (ajout colonnne où chercher une valeur)
                selectWhere += where[i] + "=?";
                // Si plusieurs where ajout du AND sauf pour le dernier
                if(i < where.length - 1) selectWhere += " AND ";
            }
            // Construction requête (ajout du WHERE)
            selectQuery += selectWhere;
        }

        // Si ORDER BY -> Construction requête
        if (orderBy != null) selectQuery += " ORDER BY " + orderBy;
        // Si LIMIT -> Construction requête
        if (limit != null) selectQuery += " LIMIT " + limit;

        // Exécution du SELECT + récupération curseur
        Cursor cursor = db.rawQuery(selectQuery, values);

        // Pour chaque élément du tableau select
        for (int i = 0; i < select.length; i++)
        {
            // On remonte le curseur
            cursor.moveToFirst();
            // Tant que le curseur n'a pas atteint la dernière ligne
            while(!cursor.isAfterLast()) {
                // On stocke la valeur de la ligne de la colonne correspondante dans le tableau à retourner à l'index correspondant au select
                resultQuery.get(i).add(cursor.getString(cursor.getColumnIndex(select[i])));
                // Ligne suivante
                cursor.moveToNext();
            }
        }

        // Fermeture curseur et BDD
        cursor.close();
        closeDB();

        return resultQuery;
    }

}
