package com.supermap.desktop.process.parameter.interfaces.datas.types;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;

/**
 * Created by highsad on 2017/3/30.
 */
public abstract class DataType {
	public abstract String getTypeName();

	public abstract Class getTypeClass();

	public class DataTypeInteger extends DataType {

		@Override
		public String getTypeName() {
			return "Integer";
		}

		@Override
		public Class getTypeClass() {
			return Integer.class;
		}
	}

	public class DataTypeDouble extends DataType {
		@Override
		public String getTypeName() {
			return "Double";
		}

		@Override
		public Class getTypeClass() {
			return Double.class;
		}
	}

	public class DataTypeFloat extends DataType {

		@Override
		public String getTypeName() {
			return null;
		}

		@Override
		public Class getTypeClass() {
			return null;
		}
	}

	public class DataTypeDataset extends DataType {
		@Override
		public String getTypeName() {
			return "Dataset";
		}

		@Override
		public Class getTypeClass() {
			return Dataset.class;
		}
	}

	public class DataTypeDatasetImage extends DataType {

		@Override
		public String getTypeName() {
			return "DatasetImage";
		}

		@Override
		public Class getTypeClass() {
			return DatasetImage.class;
		}
	}

	public class DataTypeDatasource extends DataType {
		@Override
		public String getTypeName() {
			return "Datasource";
		}

		@Override
		public Class getTypeClass() {
			return Datasource.class;
		}
	}

}
