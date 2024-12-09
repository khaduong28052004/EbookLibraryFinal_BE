package com.toel.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.model.Account;
import com.toel.model.Follower;
import com.toel.repository.AccountRepository;
import com.toel.repository.FollowerRepository;

@Service
public class FollowerService {
	@Autowired
	FollowerRepository followerRepository;
	@Autowired
	AccountRepository accountRepository;

	public boolean checkFollower(Integer id_user, Integer id_shop) {
		Account user = accountRepository.findById(id_user).get();
		try {
			Follower follower = followerRepository.findByAccountAndShopId(user, id_shop);
			if (follower.getId() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("error ================================" + e);
			return false;
		}
	}

	public boolean createFollower(Integer id_user, Integer id_shop) {
		Account user = accountRepository.findById(id_user).get();
		try {
			Follower follower = followerRepository.findByAccountAndShopId(user, id_shop);
			followerRepository.deleteById(follower.getId());
			return false;
		} catch (Exception e) {
			Follower follower = new Follower();
			follower.setAccount(user);
			follower.setShopId(id_shop);
			followerRepository.save(follower);
			return true;
		}

	}
}
