package com.eduk.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduk.domain.Board;
import com.eduk.domain.Post;
import com.eduk.repository.PostRepository;

@Service
@Transactional
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public void insert(Post post) {
		postRepository.save(post);
	}

	@Override
	public List<Post> selectAll(Long boardId) {
		return postRepository.findAll();
	}

	@Override
	public Post update(Post post) {
		Post dbPost = postRepository.findById(post.getPostId()).orElse(null);
		if(dbPost == null) throw new RuntimeException("게시물 번호 오류로 수정할 수 없습니다.");
		
		dbPost.setPostTitle(post.getPostTitle());
		dbPost.setPostContent(post.getPostContent());
		
		return dbPost;
	}

	@Override
	public void delete(Long postId) {
		Post dbPost = postRepository.findById(postId).orElse(null);
		if(dbPost == null) throw new RuntimeException("게시물 번호 오류로 삭제할 수 없습니다.");
		
		postRepository.deleteById(postId);
	}

}