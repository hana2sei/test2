package kazuate;

/**
 * ゲーム進行役 クラス
 *
 */
public class GameFacilitator {

	// 当てる数
	private String answer  = "";

// 2021/01/01 仕様変更対応 s.koga Insert Start
	// 当てる数の桁数
//	private final int ANSWER_LENGTH = 3;
	private int answerLength;
// 2021/01/01 仕様変更対応 s.koga Insert End

	// プレイヤーの入力値
	private String value;

	// 入力回数カウンタ
	private int inputCounter;

	// 入力値チェックOK時の目印
	public static final String VAL_CHECK_OK = "OK";

// 2021/01/01 仕様変更対応 s.koga Insert Start
	// 選択された難易度
	private int level;

	// 難易度を表す定数
	public static final int LEVEL_EASY = 1;
	public static final int LEVEL_NORMAL = 2;
	public static final int LEVEL_HARD = 3;

	// 難易度に対応する出題桁数を表す定数
	public static final int AS_LENGTH_EASY = 2;
	public static final int AS_LENGTH_NORMAL = 3;
	public static final int AS_LENGTH_HARD = 4;
// 2021/01/01 仕様変更対応 s.koga Insert End

	/**
	 * インスタンス生成時に、当てる数を決定しanswerフィールドに格納します。
	 */
	public GameFacilitator() {
// 2021/01/01 仕様変更対応 s.koga Update Start
//		for (int i = 0; i < this.ANSWER_LENGTH; i++) {
		setupAnswerLength();	// answerLengthを決定します

		for (int i = 0; i < this.answerLength; i++) {
// 2021/01/01 仕様変更対応 s.koga Update End
			this.answer += new java.util.Random().nextInt(9) + 1;
		}
	}

	/**
	 * キーボードから回答を受け付け、valueフィールドへ格納します。
	 */
	public void hearValue() {
//		System.out.println("テスト用: " + this.answer);
		System.out.println("----------------------------------------------------");
// 2021/01/01 仕様変更対応 s.koga Update Start
//		System.out.println(this.ANSWER_LENGTH + "桁の数字を入力してください。");
		System.out.println(this.answerLength + "桁の数字を入力してください。");
// 2021/01/01 仕様変更対応 s.koga Update End
		this.value = new java.util.Scanner(System.in).nextLine();
	}

// 2021/01/01 仕様変更対応 s.koga Insert Start
	/**
	 * キーボードから難易度の選択を受け付け、levelフィールドへ格納します。
	 */
	public void hearLevel() {
		System.out.println("難易度を選択してください。（1:EASY  2:NORMAL  3:HERD）");
		this.level = new java.util.Scanner(System.in).nextInt();
	}
// 2021/01/01 仕様変更対応 s.koga Insert End

	/**
	 * 入力値のチェックを行います。
	 * エラーの場合はエラーメッセージを戻り値として返します。正常の場合は"OK"を返します。
	 */
	public String checkValue() {
// 2021/01/01 仕様変更対応 s.koga Update Start
//		if(this.value.length() != this.ANSWER_LENGTH) {
//			return "【エラー】入力値が" + this.ANSWER_LENGTH + "桁ではありません。";
		if(this.value.length() != this.answerLength) {
			return "【エラー】入力値が" + this.answerLength + "桁ではありません。";
// 2021/01/01 仕様変更対応 s.koga Update End
		} else if(this.value.contains("0") || this.value.contains("０")) {
			return "【エラー】入力値に「0」が含まれています";
		}

		// 入力値が問題ない場合は、入力回数を+1する。
		this.inputCounter++;
		return GameFacilitator.VAL_CHECK_OK;
	}

// 2021/01/01 仕様変更対応 s.koga Insert Start
	/**
	 * 難易度の入力値のチェックを行います。
	 * エラーの場合はエラーメッセージを戻り値として返します。正常の場合は"OK"を返します。
	 */
	public String checkLevelValue() {
		if(LEVEL_EASY != this.level
			&& LEVEL_NORMAL != this.level
			&& LEVEL_HARD != this.level) {
			return "【エラー】入力値は1,2,3のいずれかとして下さい。";
		}

		return GameFacilitator.VAL_CHECK_OK;
	}

	/**
	 * 難易度に応じて、出題桁数を設定します。
	 * 難易度はキーボードから入力を受け付けます。
	 */
	public void setupAnswerLength() {
		String result = "";		// 入力値チェック結果

		// キーボードから正しい難易度入力が得られるまで、キーボード入力とチェック処理をやり直す。
		while (!GameFacilitator.VAL_CHECK_OK.equals(result)) {
			hearLevel();
			result = checkLevelValue();

			// 入力値のチェック結果がエラーの場合は、エラーメッセージを出力する。
			if(!GameFacilitator.VAL_CHECK_OK.equals(result)) {
				System.out.println(result);
			}
		}

		// 難易度の入力値を元に、answerLengthの桁数を判定し
		// 設定します。
		switch(this.level) {
			case GameFacilitator.LEVEL_EASY:
				this.answerLength = GameFacilitator.AS_LENGTH_EASY;
				break;
			case GameFacilitator.LEVEL_NORMAL:
				this.answerLength = GameFacilitator.AS_LENGTH_NORMAL;
				break;
			case GameFacilitator.LEVEL_HARD:
				this.answerLength = GameFacilitator.AS_LENGTH_HARD;
		}
	}
// 2021/01/01 仕様変更対応 s.koga Insert End

	/**
	 * 入力値が当てる数と合っているかをチェックします。
	 * このメソッドではフィールド変数valueに不正な値が入っていないことを前提としています。
	 * （checkValueメソッドを事前に呼んでいることを想定）
	 *
	 * このメソッドは戻り値として以下を返します。
	 * true		：正解の場合
	 * false	：不正解の場合
	 */
	public boolean judgeAnswer() {
		int hitCount = 0;		//「部分正解」の数

		// 1桁ずつ、「部分正解」の判定を行う。
// 2021/01/01 仕様変更対応 s.koga Update Start
//		for(int i = 0; i < this.ANSWER_LENGTH; i++) {
		for(int i = 0; i < this.answerLength; i++) {
// 2021/01/01 仕様変更対応 s.koga Update End
			char ansC  = this.answer.charAt(i);
			char valC  = this.value.charAt(i);

			if(ansC == valC) {
				// 値が一致しているので、「部分正解」の数を＋１する。
				hitCount++;
			}
		}

		// 結果を画面に出力する。
// 2021/01/01 仕様変更対応 s.koga Update Start
//		if(hitCount == this.ANSWER_LENGTH) {
		if(hitCount == this.answerLength) {
// 2021/01/01 仕様変更対応 s.koga Update End
			// 入力値が当てる数と全て一致している場合は正解と判定する。
			System.out.println("正解です！");
			System.out.println("正解までの回答数は" + this.inputCounter + "回でした。");

			return true;
		} else {
			// それ以外の場合は、不正解と判定する。
			System.out.println("不正解です。");
			System.out.println("部分正解：" + hitCount);

			return false;
		}
	}
}
