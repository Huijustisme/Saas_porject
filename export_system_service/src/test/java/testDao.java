import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
//import com.itheima.service.company.CompanyService;
//import com.itheima.service.company.impl.CompanyImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)//spring整合junit
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")//读取spring配置文件
public class testDao {
   /*@Autowired//ioc注入
    private CompanyService companyService;

   @Test
    public void  testFindAll(){
       List<Company> list = companyService.findAll();
       for (Company company : list) {
           System.out.println(company);
       }
       System.out.println(companyService);
   }

    @Test
    public void testFindByPage(){
        PageInfo pageInfo = companyService.findByPage(1,3);
        System.out.println("总页数："+pageInfo.getPages());
        System.out.println("总记录数："+pageInfo.getTotal());
        System.out.println("当前页码："+pageInfo.getPageNum());
        System.out.println("上一页："+pageInfo.getPrePage());
        System.out.println("下一页："+pageInfo.getNextPage());
        System.out.println("当前页列表："+pageInfo.getList());
    }*/
}