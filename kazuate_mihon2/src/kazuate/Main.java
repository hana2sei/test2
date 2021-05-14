package kazuate;

public class Main {
	public static void main(String[] args) {

		// ゲーム進行役インスタンスを生成
		GameFacilitator ng = new GameFacilitator();

		// ゲームクリア（正解する）まで、キーボード入力～正解判定までの一連の処理をループ
		do{
			String result = "";		// 入力値チェック結果

			// キーボードから正しい入力値が得られるまで、キーボード入力とチェック処理をやり直す。
			while (!GameFacilitator.VAL_CHECK_OK.equals(result)) {
				ng.hearValue();				// キーボード入力受付
				result = ng.checkValue();	// 入力値チェック処理

				// 入力値のチェック結果がエラーの場合は、エラーメッセージを出力する。
				if(!GameFacilitator.VAL_CHECK_OK.equals(result)) {
					System.out.println(result);
				}
			}

		} while(!ng.judgeAnswer());
	}
}
