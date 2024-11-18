package com.bmanager.dirs.model;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(DirModel.class)
public abstract class DirModel_ {

	public static final String NAME = "name";
	public static final String DATETIME_CREATED = "datetime_created";
	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String PARENT_ID = "dirId";

	
	/**
	 * @see com.bmanager.dirs.model.DirModel#name
	 **/
	public static volatile SingularAttribute<DirModel, String> name;
	
	/**
	 * @see com.bmanager.dirs.model.DirModel#datetime_created
	 **/
	public static volatile SingularAttribute<DirModel, LocalDateTime> datetime_created;
	
	/**
	 * @see com.bmanager.dirs.model.DirModel#id
	 **/
	public static volatile SingularAttribute<DirModel, Long> id;
	
	/**
	 * @see com.bmanager.dirs.model.DirModel
	 **/
	public static volatile EntityType<DirModel> class_;
	
	/**
	 * @see com.bmanager.dirs.model.DirModel#userId
	 **/
	public static volatile SingularAttribute<DirModel, Long> userId;
	
	/**
	 * @see com.bmanager.dirs.model.DirModel#parentId
	 **/
	public static volatile SingularAttribute<DirModel, Long> parentId;

}

