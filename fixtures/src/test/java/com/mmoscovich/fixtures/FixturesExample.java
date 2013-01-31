package com.mmoscovich.fixtures;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mmoscovich.fixtures.domain.Device;
import com.mmoscovich.fixtures.domain.User;
import com.mmoscovich.fixtures.store.EntityStore;
import com.mmoscovich.fixtures.test.Fixtures;
import com.mmoscovich.fixtures.test.FixturesMockitoJUnitRunner;
import com.mmoscovich.fixtures.test.annotation.FixtureEntity;
import com.mmoscovich.fixtures.test.annotation.FixtureGroup;
import com.mmoscovich.fixtures.test.annotation.FixtureGroups;
import com.mmoscovich.fixtures.test.annotation.FixtureVersion;

@RunWith(FixturesMockitoJUnitRunner.class)
@FixtureVersion("test")
public class FixturesExample {

	@Mock
	private EntityStore store;
	
	@FixtureEntity("joaoronaldo")
	private User joao;
	
	@FixtureEntity("kaka")
	private User kaka;
	
	@FixtureEntity(maxNumber=2)
	private List<User> users;
	
	@FixtureEntity
	private List<Device> devices;
	
	@Test
	@FixtureGroups({@FixtureGroup(value="all", version="default"), @FixtureGroup(value="other", version="default")})
	public void newSample() {
		Mockito.when(store.getAnyEntity(String.class)).thenReturn("HOLA");
		User juan = Fixtures.getEntity(User.class, "juanperez");
		System.out.println(juan.getLastname());
		
		System.out.println(joao.getLastname());
		System.out.println(store.getAnyEntity(String.class));
		for(Device device : devices) {
			System.out.println(device.getBrand() + " - " + device.getModel());
		}
	}
	
	@Test
	@FixtureGroups(@FixtureGroup(value="other", version="test"))
	public void newSample2() {
		User pedro = Fixtures.getEntity(User.class, "pedro");
		
		for(User user : users) {
			System.out.println(user.getLastname());
		}

		for(Device device : devices) {
			System.out.println(device.getBrand() + " - " + device.getModel());
		}
		
	}
	
}
