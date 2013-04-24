package com.edroid.demo.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.edroid.demo.db.AccountDatabase;
import com.edroid.demo.db.AccountDatabase.CategoryCols;
import com.edroid.demo.models.Category;

public class CategoryDaoTest extends InstrumentationTestCase {
    
    private Context context;
    private AccountDatabase db;
    private CategoryDao dao;

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        
        context = getInstrumentation().getTargetContext();
        db = AccountDatabase.getInstance(context);
        dao = new CategoryDao(db.getOpenHelper());
    }
    
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
    
    private Category createCategory() {
        Category c = new Category();
        c.setId(123);
        c.setName("分类1");
        return c;
    }
    
    // 增
    public void testInsert() {
        Category row = createCategory();
        long id = dao.insert(row);
        assertTrue(id > 0);
        
        // cleanup
        dao.delete(id);
    }
    
    // 删
    public void testDelete() {
        Category row = createCategory();
        long id = dao.insert(row);
        assertTrue(id > 0);

        int count = dao.delete(id);
        assertTrue(count > 0);
    }
    
    // 改
    public void testUpdate() {
        Category row = createCategory();
        long id = dao.insert(row);
        assertTrue(id > 0);
        System.out.println("origin: " + row);
        
        // change name
        String newname = "newname";
        ContentValues values = new ContentValues();
        values.put(CategoryCols.NAME, newname);
        int count = dao.update(id, values);
        assertTrue(count > 0);
        
        row = dao.findById(id);
        assertNotNull(row);
        assertEquals(newname, row.getName());
        
        // cleanup
        dao.delete(id);
    }
    
    // 查
    public void testQuery() {
        Category row = createCategory();
        long id = dao.insert(row);
        assertTrue(id > 0);
        System.out.println("origin: " + row);
        
        // find by id
        row = dao.findById(id);
        assertNotNull(row);
        assertEquals(id, row.getId());
        
        // find all
        List<Category> list = dao.findAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
        
        // find one by fields
        row = dao.findOneByFields(null, CategoryCols._ID + "=" + id, null, null);
        assertNotNull(row);
        assertEquals(id, row.getId());
        
        // find list by fields
        list = dao.findListByFields(null, CategoryCols._ID + "=" + id, null, null);
        assertNotNull(list);
        assertTrue(list.size() > 0);
        for (Category c : list) {
            assertNotNull(c);
            assertEquals(id, c.getId());
        }
        
        // cleanup
        dao.delete(id);
    }


}
