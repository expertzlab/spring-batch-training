package com.ksgbabu.batch.training;

import com.ksgbabu.batch.training.domain.Product;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by gireeshbabu on 08/03/17.
 */
public class ProductJDBCItemWriter implements ItemWriter<Product> {

    private static final String INSERT_PRODUCT = "insert into product "+
            "(id,name,description,price) values(?,?,?,?)";
    private static final String UPDATE_PRODUCT = "update product set "+
            "name=?, description=?, price=? where id=?";

    private JdbcTemplate jdbcTemplate;

    public ProductJDBCItemWriter(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public void write(List<? extends Product> items) throws Exception {
        for (Product product:items){
           int updated = jdbcTemplate.update(UPDATE_PRODUCT,
                   product.getName(),product.getDescription(),
                   product.getPrice(),product.getId());
            if(updated == 0){
                jdbcTemplate.update(INSERT_PRODUCT,product.getId(),
                        product.getName(),product.getDescription(),
                        product.getPrice());
            }
        }
    }
}
