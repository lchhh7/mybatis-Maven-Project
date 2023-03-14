package com.jinjin.jintranetTest;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.schedule.service.ScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/egovframework/spring/*.xml"})
public class TimeTest {
	
	@Autowired
	ScheduleService sService;
	
    public void testA() {
    	try {
    		Class<?> svoClazz = Class.forName("com.jinjin.jintranet.schedule.service.ScheduleService");
			System.out.println(svoClazz);
			
			for(Method m  : svoClazz.getDeclaredMethods()) {
				System.out.println(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void testB() {
    	System.out.println(sService.findById(new ScheduleVO().builder().id(549).build()));
    }
    
    
    @Test
    public void testC() {
    	try {
    		Class<?> clazz = Class.forName("com.jinjin.jintranet.schedule.service.ScheduleService");
    		
    		Method findByIdMethod = clazz.getDeclaredMethod("findById", ScheduleVO.class);
    		
    		Class<?> svoClazz = Class.forName("com.jinjin.jintranet.common.vo.ScheduleVO");
    		Object svoObj =  svoClazz.getConstructor(Integer.class).newInstance(200);
    		
    		findByIdMethod.invoke(sService, svoObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
    
    public void testD() {
    	try {
    		Class<?> clazz = Class.forName("com.jinjin.jintranet.common.vo.ScheduleVO");
    		Object svoObj = clazz.getConstructor(Integer.class).newInstance(200);
    		
    		Method findByIdMethod = clazz.getDeclaredMethod("", ScheduleVO.class);
    		
    		System.out.println(findByIdMethod.invoke(svoObj, svoObj));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
